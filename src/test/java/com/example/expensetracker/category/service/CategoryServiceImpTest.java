package com.example.expensetracker.category.service;

import com.example.expensetracker.auth.dto.UserContextDto;
import com.example.expensetracker.category.domain.CategoryEntity;
import com.example.expensetracker.category.dto.CategoryFilter;
import com.example.expensetracker.category.dto.CategoryRequest;
import com.example.expensetracker.category.dto.CategoryResponse;
import com.example.expensetracker.category.dto.CategoryUpdateRequest;
import com.example.expensetracker.category.mapper.CategoryMapper;
import com.example.expensetracker.category.repository.CategoryRepository;
import com.example.expensetracker.common.exception.ConflictException;
import com.example.expensetracker.common.exception.NotFoundException;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImpTest {

    @Mock
    private CategoryRepository repository;

    @Mock
    private CategoryMapper mapper;

    @InjectMocks
    private CategoryServiceImp service;

    private MockedStatic<JwtUser> jwtUserMock;

    private static final Long USER_ID = 1L;
    private static final Long CATEGORY_ID = 2L;
    private static final String NAME = "TestName";
    private static final String DESCRIPTION = "TestDescription";
    private static final String UPDATE_NAME = "TestUpdateName";
    private static final String UPDATE_DESCRIPTION = "TestUpdateDescription";
    private static final BigDecimal MONTHLY_LIMIT = BigDecimal.valueOf(1000);

    @BeforeEach
    void setUp() {
        UserContextDto userContextDto = new UserContextDto();
        userContextDto.setId(USER_ID);

        jwtUserMock = mockStatic(JwtUser.class);
        jwtUserMock.when(JwtUser::getAuthenticatedUser).thenReturn(userContextDto);
    }

    @AfterEach
    void tearDown() {
        jwtUserMock.close();
    }


    @Nested
    @DisplayName("getAll")
    class TestsForGetAll {
        @Test
        void shouldReturnFilteredCategories() {
            CategoryFilter filter = createCategoryFilter();
            CategoryEntity categoryEntity = createCategoryEntity();
            Page<CategoryEntity> page = new PageImpl<>(List.of(categoryEntity));

            when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
            when(mapper.toResponse(categoryEntity)).thenReturn(new CategoryResponse());

            Page<CategoryResponse> categoryResponses = service.getAll(filter);

            assertEquals(1, categoryResponses.getTotalElements());
            verify(repository, times(1)).findAll(any(Specification.class), any(Pageable.class));
            verify(mapper, times(1)).toResponse(categoryEntity);
        }

        @Test
        void shouldReturnsEmptyResultWhenNoCategoriesFound() {
            CategoryFilter filter = createCategoryFilter();

            when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(Page.empty());

            Page<CategoryResponse> categoryResponses = service.getAll(filter);

            assertTrue(categoryResponses.isEmpty());
            verify(repository, times(1)).findAll(any(Specification.class), any(Pageable.class));
            verify(mapper, never()).toResponse(any(CategoryEntity.class));
        }
    }


    @Nested
    @DisplayName("getById")
    class TestsForGetById {
        @Test
        void shouldReturnsCategoryResponse() {
            CategoryEntity categoryEntity = createCategoryEntity();
            CategoryResponse categoryResponse = new CategoryResponse();

            when(repository.findByIdAndUserId(CATEGORY_ID, USER_ID))
                    .thenReturn(java.util.Optional.of(categoryEntity));
            when(mapper.toResponse(categoryEntity)).thenReturn(categoryResponse);

            CategoryResponse result = service.getById(CATEGORY_ID);

            assertEquals(categoryResponse, result);
            verify(repository, times(1)).findByIdAndUserId(CATEGORY_ID, USER_ID);
            verify(mapper, times(1)).toResponse(categoryEntity);
        }

        @Test
        void shouldThrowNotFoundException() {
            when(repository.findByIdAndUserId(CATEGORY_ID, USER_ID))
                    .thenReturn(java.util.Optional.empty());

            assertThrows(NotFoundException.class, () -> service.getById(CATEGORY_ID));
            verify(repository, times(1)).findByIdAndUserId(CATEGORY_ID, USER_ID);
            verifyNoInteractions(mapper);
        }
    }

    @Nested
    @DisplayName("create")
    class TestsForCreate {
        @Test
        void shouldCreatesCategory() {
            CategoryRequest request = createCategoryRequest();
            CategoryEntity categoryEntity = createCategoryEntity();
            CategoryResponse categoryResponse = new CategoryResponse();

            when(repository.existsByUserIdAndName(USER_ID, NAME)).thenReturn(false);
            when(mapper.toResponse(any(CategoryEntity.class))).thenReturn(categoryResponse);
            doAnswer(invocation -> {
                categoryEntity.setId(CATEGORY_ID);
                return null;
            }).when(repository).save(any(CategoryEntity.class));

            CategoryResponse result = service.create(request);

            assertEquals(categoryResponse, result);
            verify(repository, times(1)).existsByUserIdAndName(USER_ID, NAME);
            verify(repository, times(1)).save(any(CategoryEntity.class));
            verify(mapper, times(1)).toResponse(any(CategoryEntity.class));
        }

        @Test
        void shouldThrowConflictException_ForDuplicateCategoryName() {
            CategoryRequest request = createCategoryRequest();

            when(repository.existsByUserIdAndName(USER_ID, NAME)).thenReturn(true);

            assertThrows(ConflictException.class, () -> service.create(request));
            verify(repository, times(1)).existsByUserIdAndName(USER_ID, NAME);
            verify(repository, never()).save(any(CategoryEntity.class));
            verifyNoInteractions(mapper);
        }
    }

    @Nested
    @DisplayName("update")
    class TestsForUpdate {
        @Test
        void shouldUpdatesCategory() {
            CategoryUpdateRequest request = createCategoryUpdateRequest();
            CategoryEntity existingEntity = createCategoryEntity();
            CategoryResponse updatedResponse = new CategoryResponse();

            when(repository.findByIdAndUserId(CATEGORY_ID, USER_ID))
                    .thenReturn(java.util.Optional.of(existingEntity));

            doAnswer(invocation -> {
                CategoryUpdateRequest req = invocation.getArgument(0);
                CategoryEntity entity = invocation.getArgument(1);

                entity.setName(req.getName());
                entity.setDescription(req.getDescription());
                entity.setMonthlyLimit(req.getMonthlyLimit());
                return null;
            }).when(mapper).updateEntity(any(CategoryUpdateRequest.class), any(CategoryEntity.class));

            when(mapper.toResponse(existingEntity)).thenReturn(updatedResponse);

            CategoryResponse result = service.update(CATEGORY_ID, request);

            assertEquals(updatedResponse, result);
            verify(repository, times(1)).findByIdAndUserId(CATEGORY_ID, USER_ID);
            verify(mapper, times(1)).updateEntity(request, existingEntity);
            verify(repository, times(1)).save(existingEntity);
            verify(mapper, times(1)).toResponse(existingEntity);
        }

        @Test
        void shouldThrowNotFoundException() {
            CategoryUpdateRequest request = createCategoryUpdateRequest();

            when(repository.findByIdAndUserId(CATEGORY_ID, USER_ID))
                    .thenReturn(java.util.Optional.empty());

            assertThrows(NotFoundException.class, () -> service.update(CATEGORY_ID, request));

            verify(repository, times(1)).findByIdAndUserId(CATEGORY_ID, USER_ID);
            verify(mapper, never()).updateEntity(any(), any());
            verify(repository, never()).save(any());
        }
    }


        private CategoryEntity createCategoryEntity() {
            return CategoryEntity.builder()
                    .id(CATEGORY_ID)
                    .name(NAME)
                    .description(DESCRIPTION)
                    .userId(USER_ID)
                    .monthlyLimit(MONTHLY_LIMIT)
                    .build();
        }

        private CategoryFilter createCategoryFilter() {
            CategoryFilter filter = new CategoryFilter();
            filter.putUserId(USER_ID);
            filter.setName(NAME);
            filter.setDescription(DESCRIPTION);
            filter.setPageSize(10);
            return filter;
        }

        private CategoryRequest createCategoryRequest() {
            CategoryRequest request = new CategoryRequest();
            request.setName(NAME);
            request.setMonthlyLimit(MONTHLY_LIMIT);
            request.setDescription(DESCRIPTION);
            return request;
        }

        private CategoryUpdateRequest createCategoryUpdateRequest() {
            CategoryUpdateRequest request = new CategoryUpdateRequest();
            request.setName(UPDATE_NAME);
            request.setDescription(UPDATE_DESCRIPTION);
            request.setMonthlyLimit(MONTHLY_LIMIT);
            return request;
        }
    }
