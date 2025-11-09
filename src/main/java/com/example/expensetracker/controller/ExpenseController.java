package com.example.expensetracker.controller;

import com.example.expensetracker.model.Expense;
import com.example.expensetracker.service.ExpenseService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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
    public ResponseEntity<Expense> addExpense(@RequestBody Expense expense) {
        Expense savedExpense = expenseService.addExpense(expense);
        return ResponseEntity.ok(savedExpense);
    }

    // 2. Read - get all expenses
    @GetMapping
    public ResponseEntity<List<Expense>> getAllExpenses() {
        List<Expense> expenses = expenseService.getAllExpense();
        return ResponseEntity.ok(expenses);
    }

    // 3. Fetch - get expenses by id
    @GetMapping("/{id}")
    public ResponseEntity<Expense> getExpenseById(@PathVariable UUID id) {
        Optional<Expense> expense = expenseService.getExpenseById(id);
        return expense.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 4. Delete - delete particular expenses by it's id (UUID)
    @DeleteMapping("/{id}")
    public ResponseEntity<Expense> deleteExpenseById(@PathVariable UUID id) {
        try {
            expenseService.deleteExpenseById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 5. Patch - update a particular expense partially by it's id (UUID), meaning without passing whole body, just pass whatever you want to update and it will do that
    @PatchMapping("/{id}")
    public ResponseEntity<Expense> updateExpensePartially(@PathVariable UUID id, @RequestBody Expense partialExpense) {
        try {
            Expense updatedExpense= expenseService.updateExpensePartially(id, partialExpense);
            return ResponseEntity.ok(updatedExpense);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
