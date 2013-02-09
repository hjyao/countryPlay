package com.trailblazers.freewheelers.web;

import com.trailblazers.freewheelers.model.Account;
import com.trailblazers.freewheelers.model.Item;
import com.trailblazers.freewheelers.model.ReserveOrder;
import com.trailblazers.freewheelers.service.AccountService;
import com.trailblazers.freewheelers.service.ItemService;
import com.trailblazers.freewheelers.service.ReserveOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.Date;

@Controller
@RequestMapping(ReserveController.URL)
public class ReserveController {

    static final String URL = "/reserve";

    @Autowired
    ItemService itemService;

    @Autowired
    AccountService accountService;

    @Autowired
    ReserveOrderService reserveOrderService;

    @RequestMapping(method = RequestMethod.GET)
    public void get(Model model) {
    }

    //TODO: display all orders of customers instead
    @RequestMapping(method = RequestMethod.POST, params="reserve=Reserve Item")
    public void reserveItem(Model model, Principal principal, @ModelAttribute("itemCommand") ItemCommand itemCommand){
        Item item =  itemService.get(itemCommand.getItemId());
        String userName = principal.getName();
        Account account =  accountService.getAccountIdByName(userName);
        Date rightNow = new Date();

        ReserveOrder reserveOrder = new ReserveOrder(account.getAccount_id(), item.getItemId(), rightNow );

        reserveOrderService.save(reserveOrder);
        itemService.decreaseQuantityByOne(item);

        model.addAttribute("item", item);
    }




}