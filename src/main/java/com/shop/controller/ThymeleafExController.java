package com.shop.controller;

import com.shop.dto.ItemDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/thymeleaf")
public class ThymeleafExController {


    @GetMapping(value="/ex01")
    public String ex01(Model model){
        model.addAttribute("data","타임리프 예제1_!_!_!_");
        return "thymeleafEx/thymeleafEx01";
    }

    @GetMapping(value="/ex02")
    public String ex02(Model model){

        ItemDto itemDto = new ItemDto();
        itemDto.setItemDetail("상세설명1");
        itemDto.setItemNm("상품1");
        itemDto.setPrice(10000);
        itemDto.setRegTime(LocalDateTime.now());

        model.addAttribute("itemDto",itemDto);

        return "thymeleafEx/thymeleafEx02";
    }
    @GetMapping(value="/ex03")
    public String ex03(Model model){
        List<ItemDto> itemList = new ArrayList<>();
        for(int i=1; i<11; i++){
            ItemDto itemDto = new ItemDto();
            itemDto.setItemDetail("상세설명"+i);
            itemDto.setItemNm("상품"+i);
            itemDto.setPrice(10000*i);
            itemDto.setRegTime(LocalDateTime.now());
            itemList.add(itemDto);
        }

        model.addAttribute("itemList",itemList);

        return "thymeleafEx/thymeleafEx03";
    }
    @GetMapping(value="/ex04")
    public String ex04(Model model){
        List<ItemDto> itemList = new ArrayList<>();
        for(int i=1; i<11; i++){
            ItemDto itemDto = new ItemDto();
            itemDto.setItemDetail("상세설명"+i);
            itemDto.setItemNm("상품"+i);
            itemDto.setPrice(10000*i);
            itemDto.setRegTime(LocalDateTime.now());
            itemList.add(itemDto);
        }

        model.addAttribute("itemList",itemList);

        return "thymeleafEx/thymeleafEx04";
    }

    @GetMapping(value="/ex05")
    public String ex05(Model model){

        return "thymeleafEx/thymeleafEx05";
    }

    @GetMapping(value="/ex06")
    public String ex06(String param1, String param2, Model model){
        model.addAttribute("param1", param1);
        model.addAttribute("param2", param2);

        return "thymeleafEx/thymeleafEx06";
    }

    @GetMapping(value="/ex07")
    public String ex07(){

        return "thymeleafEx/thymeleafEx07";
    }
}
