package com.example.saga.inventory.service;

import com.example.saga.inventory.entity.Inventory;
import com.example.saga.inventory.repository.InventoryRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

	@Mock
	private InventoryRepository inventoryRepository;

	@InjectMocks
	private InventoryService inventoryService;

	private Inventory inventory;

	@BeforeEach
	void setUp() {

		inventory = new Inventory();

		inventory.setProductId(100L);
		inventory.setAvailableQuantity(10);
	}

	@Test
	void shouldReserveInventorySuccessfully() {

		when(inventoryRepository.findById(100L)).thenReturn(Optional.of(inventory));

		when(inventoryRepository.save(any(Inventory.class))).thenReturn(inventory);

		boolean result = inventoryService.reserveInventory(100L, 2);

		assertTrue(result);

		assertEquals(8, inventory.getAvailableQuantity());

		verify(inventoryRepository, times(1)).findById(100L);

		verify(inventoryRepository, times(1)).save(inventory);
	}

	@Test
	void shouldReturnFalseWhenInventoryIsInsufficient() {

		when(inventoryRepository.findById(100L)).thenReturn(Optional.of(inventory));

		boolean result = inventoryService.reserveInventory(100L, 20);

		assertFalse(result);

		assertEquals(10, inventory.getAvailableQuantity());

		verify(inventoryRepository, times(1)).findById(100L);

		verify(inventoryRepository, never()).save(any(Inventory.class));
	}

	@Test
	void shouldThrowExceptionWhenProductDoesNotExist() {

		when(inventoryRepository.findById(100L)).thenReturn(Optional.empty());

		assertThrows(RuntimeException.class, () -> inventoryService.reserveInventory(100L, 2));

		verify(inventoryRepository, times(1)).findById(100L);

		verify(inventoryRepository, never()).save(any(Inventory.class));
	}

	@Test
	void shouldThrowExceptionWhenReserveSaveFails() {

		when(inventoryRepository.findById(100L)).thenReturn(Optional.of(inventory));

		when(inventoryRepository.save(any(Inventory.class))).thenThrow(new RuntimeException("Database error"));

		assertThrows(RuntimeException.class, () -> inventoryService.reserveInventory(100L, 2));

		assertEquals(8, inventory.getAvailableQuantity());

		verify(inventoryRepository, times(1)).save(any(Inventory.class));
	}

	@Test
	void shouldReleaseInventorySuccessfully() {

		inventory.setAvailableQuantity(8);

		when(inventoryRepository.findById(100L)).thenReturn(Optional.of(inventory));

		when(inventoryRepository.save(any(Inventory.class))).thenReturn(inventory);

		inventoryService.releaseInventory(100L, 2);

		assertEquals(10, inventory.getAvailableQuantity());

		verify(inventoryRepository, times(1)).findById(100L);

		verify(inventoryRepository, times(1)).save(inventory);
	}

	@Test
	void shouldThrowExceptionWhenProductDoesNotExistDuringRelease() {

		when(inventoryRepository.findById(100L)).thenReturn(Optional.empty());

		assertThrows(RuntimeException.class, () -> inventoryService.releaseInventory(100L, 2));

		verify(inventoryRepository, times(1)).findById(100L);

		verify(inventoryRepository, never()).save(any(Inventory.class));
	}

	@Test
	void shouldThrowExceptionWhenReleaseSaveFails() {

		inventory.setAvailableQuantity(8);

		when(inventoryRepository.findById(100L)).thenReturn(Optional.of(inventory));

		when(inventoryRepository.save(any(Inventory.class))).thenThrow(new RuntimeException("Database error"));

		assertThrows(RuntimeException.class, () -> inventoryService.releaseInventory(100L, 2));

		assertEquals(10, inventory.getAvailableQuantity());

		verify(inventoryRepository, times(1)).save(any(Inventory.class));
	}
}