package com.example.expensetracker.service;

import com.example.expensetracker.model.Expense;
import com.example.expensetracker.repository.ExpenseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service //service layer is where business logic is kept (this is managed as Spring Bean), it acts a bridge controller (handles HTTP requests) and the repository (handles DB operations)
public class ExpenseService {

    @Autowired // spring automatically injects the Repository dependency here
    private ExpenseRepository expenseRepository;

    // 1. Create  (Add new Expense)
    public Expense addExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    // 2. Read (Get all expenses)
    public List<Expense> getAllExpense() {
        return expenseRepository.findAll();
    }

    // 3. Read (find expense by id)
    public Optional<Expense> getExpenseById(UUID id) {
        return expenseRepository.findById(id);
    }

    // 4. Delete (delete expense by id)
    public void deleteExpenseById(UUID id) {
        if (!expenseRepository.existsById(id)) {
            throw new EntityNotFoundException("Expense with id " + id + " not found");
        }
        expenseRepository.deleteById(id);
    }

    public Expense updateExpensePartially(UUID id, Expense partialExpense) {
        Expense existingExpense = expenseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Expense with id " + id + " not found"));
        if (partialExpense.getExpenseTitle() != null) {
            existingExpense.setExpenseTitle(partialExpense.getExpenseTitle());
        }

        if (partialExpense.getAmount() != null) {
            existingExpense.setAmount(partialExpense.getAmount());
        }

        if (partialExpense.getNotes() != null) {
            existingExpense.setNotes(partialExpense.getNotes());
        }

        if(partialExpense.getDate() != null) {
            existingExpense.setDate(partialExpense.getDate());
        }

        return expenseRepository.save(existingExpense);
    }
}
