package com.example.expensetracker.expense.service;

import com.example.expensetracker.auth.dto.UserContextDto;
import com.example.expensetracker.category.domain.CategoryEntity;
import com.example.expensetracker.category.service.CategoryService;
import com.example.expensetracker.common.exception.BadRequestException;
import com.example.expensetracker.common.exception.ExceptionModel;
import com.example.expensetracker.common.exception.NotFoundException;
import com.example.expensetracker.common.util.DateUtil;
import com.example.expensetracker.expense.domain.ExpenseEntity;
import com.example.expensetracker.expense.dto.ExpenseFilter;
import com.example.expensetracker.expense.dto.ExpenseRequest;
import com.example.expensetracker.expense.dto.ExpenseResponse;
import com.example.expensetracker.expense.mapper.ExpenseMapper;
import com.example.expensetracker.expense.repository.ExpenseRepository;
import com.example.expensetracker.security.jwt.JwtUser;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExpenseServiceImpTest {


    @Mock
    private ExpenseRepository repository;

    @Mock
    private CategoryService categoryService;

    @Mock
    private ExpenseMapper mapper;

    @InjectMocks
    private ExpenseServiceImp service;

    private MockedStatic<JwtUser> jwtUserMock;
    private MockedStatic<DateUtil> dateUtilMock;

    private static final Long USER_ID = 1L;
    private static final Long CATEGORY_ID = 2L;
    private static final Long EXPENSE_ID = 1L;
    private static final BigDecimal AMOUNT = BigDecimal.valueOf(50.0);
    private static final String NAME = "TestName";
    private static final int YEAR = 2024;
    private static final int MONTH = 10;
    private static final LocalDateTime EXPENSE_DATE = LocalDateTime.of(YEAR, MONTH, 5, 10, 0);
    private static final BigDecimal MONTHLY_LIMIT = BigDecimal.valueOf(1000);


    @BeforeEach
    void setUp() {
        UserContextDto userContextDto = new UserContextDto();
        userContextDto.setId(USER_ID);

        dateUtilMock = mockStatic(DateUtil.class);
        jwtUserMock = mockStatic(JwtUser.class);
        jwtUserMock.when(JwtUser::getAuthenticatedUser).thenReturn(userContextDto);
    }

    @AfterEach
    void tearDown() {
        jwtUserMock.close();
        dateUtilMock.close();
    }


    @Nested
    @DisplayName("getById")
    class TestsForGetById {
        @Test
        void shouldReturnExpenseResponseWhenValid() {
            ExpenseEntity expense = createExpense();
            ExpenseResponse expectedResponse = new ExpenseResponse();

            when(repository.findByIdAndUserIdWithCategory(EXPENSE_ID, USER_ID)).thenReturn(Optional.of(expense));
            when(mapper.toResponse(expense)).thenReturn(expectedResponse);

            ExpenseResponse response = service.getById(EXPENSE_ID, CATEGORY_ID);

            assertEquals(expectedResponse, response);
            verify(repository, times(1)).findByIdAndUserIdWithCategory(EXPENSE_ID, USER_ID);
            verify(mapper, times(1)).toResponse(expense);
        }

        @Test
        void shouldThrowNotFoundExceptionWhenExpenseNotFound() {
            when(repository.findByIdAndUserIdWithCategory(EXPENSE_ID, USER_ID)).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> service.getById(EXPENSE_ID, CATEGORY_ID));
            verify(repository, times(1)).findByIdAndUserIdWithCategory(EXPENSE_ID, USER_ID);
        }

        @Test
        void shouldThrowBadRequestExceptionWhenExpenseIsNotBelongToCategory() {
            ExpenseEntity expense = new ExpenseEntity();
            expense.setId(EXPENSE_ID);
            expense.setCategoryId(999L);

            when(repository.findByIdAndUserIdWithCategory(EXPENSE_ID, USER_ID)).thenReturn(Optional.of(expense));

            assertThrows(BadRequestException.class, () -> service.getById(EXPENSE_ID, CATEGORY_ID));
            verify(repository, times(1)).findByIdAndUserIdWithCategory(EXPENSE_ID, USER_ID);
        }
    }


    @Nested
    @DisplayName("getAll")
    class TestsForGetAll {

        @Test
        void shouldReturnFilteredExpenses() {
            ExpenseFilter filter = createExpenseFilter();
            ExpenseEntity expenseEntity = createExpense();
            Page<ExpenseEntity> page = new PageImpl<>(List.of(expenseEntity));

            when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
            when(mapper.toResponse(expenseEntity)).thenReturn(new ExpenseResponse());

            Page<ExpenseResponse> expenseResponses = service.getAll(filter);

            assertEquals(1, expenseResponses.getTotalElements());
            verify(repository, times(1)).findAll(any(Specification.class), any(Pageable.class));
            verify(mapper, times(1)).toResponse(expenseEntity);
        }

        @Test
        void shouldReturnsEmptyResultWhenNoExpensesExist() {
            ExpenseFilter filter = createExpenseFilter();

            when(repository.findAll(any(Specification.class), eq(filter.toPageable()))).thenReturn(Page.empty());

            Page<ExpenseResponse> expenseResponses = service.getAll(filter);

            assertNotNull(expenseResponses);
            assertTrue(expenseResponses.isEmpty());
            verify(repository, times(1)).findAll(any(Specification.class), eq(filter.toPageable()));
        }
    }

    @Nested
    @DisplayName("create")
    class TestsForCreate {
        @Test
        void shouldCreatesExpense() {
            ExpenseRequest request = createExpenseRequest();
            CategoryEntity category = createCategory();
            ExpenseEntity expense = createExpense();

            ExpenseResponse response = new ExpenseResponse();

            when(categoryService.findByIdAndUserIdOrThrowException(CATEGORY_ID, USER_ID)).thenReturn(category);
            when(repository.save(any(ExpenseEntity.class))).thenReturn(expense);
            when(mapper.toResponse(expense)).thenReturn(response);
            doAnswer(invocation -> {
                expense.setId(CATEGORY_ID);
                return null;
            }).when(repository).save(any(ExpenseEntity.class));

            ExpenseResponse result = service.create(request, CATEGORY_ID);

            assertNotNull(result);
            assertEquals(response, result);
            verify(repository, times(1)).save(any(ExpenseEntity.class));
            verify(mapper, times(1)).toResponse(expense);
        }

        @Test
        void shouldThrowBadRequestExceptionWhenExpenseDateIsInFuture() {
            ExpenseRequest request = createExpenseRequest();
            request.setExpenseDate(LocalDateTime.now().plusDays(1));

            assertThrows(BadRequestException.class, () -> service.create(request, CATEGORY_ID));
            verify(repository, never()).save(any(ExpenseEntity.class));
        }

        @Test
        void shouldThrowBadRequestExceptionWhenCategoryNotFound() {
            ExpenseRequest request = createExpenseRequest();

            when(categoryService.findByIdAndUserIdOrThrowException(CATEGORY_ID, USER_ID))
                    .thenThrow(new NotFoundException(new ExceptionModel()));

            assertThrows(NotFoundException.class, () -> service.create(request, CATEGORY_ID));
            verify(repository, never()).save(any(ExpenseEntity.class));
        }
    }


    @Nested
    @DisplayName("delete")
    class TestsForDelete {
        @Test
        void shouldReturnTrueWhenExpenseIsDeleted() {
            ExpenseEntity expense = createExpense();

            when(repository.findByIdAndUserIdWithCategory(EXPENSE_ID, USER_ID)).thenReturn(Optional.of(expense));

            assertTrue(service.delete(EXPENSE_ID, CATEGORY_ID));
            verify(repository, times(1)).delete(expense);
        }

        @Test
        void shouldThrowNotFoundExceptionWhenExpenseNotFound() {
            when(repository.findByIdAndUserIdWithCategory(EXPENSE_ID, USER_ID)).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> service.delete(EXPENSE_ID, CATEGORY_ID));
            verify(repository, never()).delete(any(ExpenseEntity.class));
        }

        @Test
        void shouldThrowBadRequestExceptionWhenExpenseIsNotBelongToCategory() {
            ExpenseEntity expense = new ExpenseEntity();
            expense.setId(EXPENSE_ID);
            expense.setCategoryId(999L);

            when(repository.findByIdAndUserIdWithCategory(EXPENSE_ID, USER_ID)).thenReturn(Optional.of(expense));

            assertThrows(BadRequestException.class, () -> service.delete(EXPENSE_ID, CATEGORY_ID));
            verify(repository, never()).delete(expense);
        }

    }




    private ExpenseEntity createExpense() {
        return ExpenseEntity.builder()
                .id(EXPENSE_ID)
                .categoryId(CATEGORY_ID)
                .build();
    }

    private ExpenseFilter createExpenseFilter() {
        ExpenseFilter filter = new ExpenseFilter();
        filter.putUserId(USER_ID);
        return filter;
    }

    private ExpenseRequest createExpenseRequest() {
        ExpenseRequest request = new ExpenseRequest();
        request.setName(NAME);
        request.setAmount(AMOUNT);
        request.setExpenseDate(EXPENSE_DATE);
        return request;
    }

    private CategoryEntity createCategory() {
        return CategoryEntity.builder()
                .id(CATEGORY_ID)
                .userId(USER_ID)
                .monthlyLimit(MONTHLY_LIMIT)
                .build();
    }
}
