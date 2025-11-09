package com.example.expensetracker.controller;

import com.example.expensetracker.dto.ApiResponse;
import com.example.expensetracker.model.Expense;
import com.example.expensetracker.service.ExpenseService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
    @Autowired // basically we are injecting our service class here since controller does not know about repository directly
    private ExpenseService expenseService;

    // 1. Create - Add a new Expense
    @PostMapping
    public ResponseEntity<ApiResponse<Expense>> addExpense(@RequestBody Expense expense) {
        Expense savedExpense = expenseService.addExpense(expense);
        return ResponseEntity.ok(new ApiResponse<>("Expense created successfully", savedExpense));
    }

    // 2. Read - get all expenses
    @GetMapping
    public ResponseEntity<ApiResponse<List<Expense>>> getAllExpenses() {
        List<Expense> expenses = expenseService.getAllExpense();
        return ResponseEntity.ok(new ApiResponse<>("Fetched all expenses till date successfully", expenses));
    }

    // 3. Fetch - get expenses by id
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Expense>> getExpenseById(@PathVariable UUID id) {
        Optional<Expense> expense = expenseService.getExpenseById(id);
        return expense
                .map(value -> ResponseEntity.ok(new ApiResponse<>("Expense fetched successfully", value)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).
                        body(new ApiResponse<>("Expense not found with provided id: "+id)));
    }

    // 4. Delete - delete particular expenses by it's id (UUID)
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteExpenseById(@PathVariable UUID id) {
        try {
            expenseService.deleteExpenseById(id);
            return ResponseEntity.ok(new ApiResponse<>("Expense deleted successfully"));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>("Could not delete the Expense as no expense found with provided id: "+id));
        }
    }

    // 5. Patch - update a particular expense partially by its id (UUID), meaning without passing whole body, just pass whatever you want to update and it will do that
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Expense>> updateExpensePartially(@PathVariable UUID id, @RequestBody Expense partialExpense) {
        try {
            Expense updatedExpense= expenseService.updateExpensePartially(id, partialExpense);
            return ResponseEntity.ok(new ApiResponse<>("Expense updated successfully", updatedExpense));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>("Could not update the Expense as no expense found with provided id: "+id));
        }
    }
}
