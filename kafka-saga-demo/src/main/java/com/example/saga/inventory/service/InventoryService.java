package com.example.saga.inventory.service;

import com.example.saga.inventory.entity.Inventory;
import com.example.saga.inventory.repository.InventoryRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {

	private static final Logger log = LoggerFactory.getLogger(InventoryService.class);

	private final InventoryRepository inventoryRepository;

	public InventoryService(InventoryRepository inventoryRepository) {

		this.inventoryRepository = inventoryRepository;
	}

	@Transactional
	public boolean reserveInventory(Long productId, Integer quantity) {

		try {

			Inventory inventory = inventoryRepository.findById(productId)
					.orElseThrow(() -> new RuntimeException("Product not found"));

			if (inventory.getAvailableQuantity() < quantity) {

				log.warn("Insufficient inventory. productId={}", productId);

				return false;
			}

			inventory.setAvailableQuantity(inventory.getAvailableQuantity() - quantity);

			inventoryRepository.save(inventory);

			log.info("Inventory reserved. productId={}, quantity={}", productId, quantity);

			return true;

		} catch (Exception e) {

			log.error("Inventory reservation failed", e);

			throw new RuntimeException("Inventory reservation failed", e);
		}
	}

	@Transactional
	public void releaseInventory(Long productId, Integer quantity) {

		try {

			Inventory inventory = inventoryRepository.findById(productId)
					.orElseThrow(() -> new RuntimeException("Product not found"));

			inventory.setAvailableQuantity(inventory.getAvailableQuantity() + quantity);

			inventoryRepository.save(inventory);

			log.info("Inventory released. productId={}, quantity={}", productId, quantity);

		} catch (Exception e) {

			log.error("Inventory rollback failed", e);

			throw new RuntimeException("Inventory rollback failed", e);
		}
	}
}