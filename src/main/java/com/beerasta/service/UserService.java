package com.beerasta.service;

import com.beerasta.domain.Item;
import com.beerasta.domain.User;
import com.beerasta.repository.UsersRepository;
import com.beerasta.web.rest.errors.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    private final UsersRepository usersRepository;
    private final ItemService itemService;

    public User findByUsername(String userName) throws NotFoundException {
        return usersRepository.findByUsername(userName)
                .orElseThrow(() -> new NotFoundException("No user with username " + userName));
    }

    public User save(User user) {
        return usersRepository.save(user);
    }

    public List<Item> getBookedItems(String userName) throws NotFoundException {
        User user = findByUsername(userName);
        log.info(user.toString());
        List<Item> result = new ArrayList<>();
        itemService.getAllItemsByUser().forEach(i -> {
           if (i.getVisitors().stream().anyMatch(v -> v.equals(user))) {
               result.add(i);
           }
        });
        return result;
    }
/*
    public List<Item> getPersonalItems(String userName) throws NotFoundException {
        User user = findByUsername(userName);
        log.info(user.toString());
        return user.getPersonalItems();
    }

    public User deleteBookedItem(User user, Long itemId) throws NotFoundException {
        Item item = itemService.getItemById(itemId);
        log.info(item.toString());
        user.getBookedItems().remove(item);
        return usersRepository.save(user);
    }

    public User deletePersonalItem(User user, Long itemId) throws NotFoundException {
        Item item = itemService.getItemById(itemId);
        log.info(item.toString());
        user.getPersonalItems().remove(item);
        return usersRepository.save(user);
    }*/

    public User addBookedItem(User user, Long itemId) {
        Item item = itemService.getItemById(itemId);
        item.getVisitors().add(user);
        log.info(item.toString());
        itemService.addItem(item);
        return user;
    }

    public User addPersonalItem(User user, Item item) {
        item.setOwner(user);
        itemService.addItem(item);
        return user;
    }
}
