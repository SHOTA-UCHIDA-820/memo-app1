package com.example.memoapp1.controller;

import com.example.memoapp1.entity.Memo;
import com.example.memoapp1.entity.Tag;
import com.example.memoapp1.service.MemoService;
import com.example.memoapp1.service.TagService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/memos")
public class MemoController {

    private final MemoService memoService;
    private final TagService tagService;

    public MemoController(MemoService memoService, TagService tagService) {
        this.memoService = memoService;
        this.tagService = tagService;
    }

	//メモ一覧取得
    @GetMapping("")
    public String getMypage(Model model) {
        model.addAttribute("memos", memoservice.getAllMemos());
        return "mypage";
    }  
    //新規メモ作成画面
    
    
    //新規メモ作成処理
    
    //メモ詳細画面表示
    
    //メモ編集画面表示
    
    //メモ編集処理
    
    //メモ削除処理
}