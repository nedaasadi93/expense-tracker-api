package com.example.expensetracker.expense.service;

import com.example.expensetracker.auth.dto.UserContextDto;
import com.example.expensetracker.category.service.CategoryService;
import com.example.expensetracker.common.exception.BadRequestException;
import com.example.expensetracker.common.exception.NotFoundException;
import com.example.expensetracker.common.util.DateUtil;
import com.example.expensetracker.expense.domain.ExpenseEntity;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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




    private ExpenseEntity createExpense() {
        return ExpenseEntity.builder()
                .id(EXPENSE_ID)
                .categoryId(CATEGORY_ID)
                .build();
    }
}
