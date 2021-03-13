package com.jojoldu.book.springboot.service;


import com.jojoldu.book.springboot.entities.ItemEntity;
import com.jojoldu.book.springboot.entities.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Transactional
@Service
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;

    public List<ItemEntity> findAll(){
        List<ItemEntity> items = new ArrayList<>();
        itemRepository.findAll().forEach(e->items.add(e));
        return items;
    }
    public Optional<ItemEntity> findById(Long id) {
        Optional<ItemEntity> item = itemRepository.findById(id);
        return item;
    }
    public void deleteById(Long id) {
        itemRepository.deleteById(id);
    }
    public ItemEntity save(ItemEntity item) {
        System.out.println(item);
        itemRepository.save(item);
        return item;
    }
    public void updateById(Long id, ItemEntity item) {
        Optional<ItemEntity> e = itemRepository.findById(id);
        if (e.isPresent()) {
            e.get().setId(item.getId());
            e.get().setTitle(item.getTitle());
            e.get().setPagename(item.getPagename());
            itemRepository.save(item);
        }
    }

}
