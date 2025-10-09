package com.example.memoapp1.controller;

import com.example.memoapp1.entity.Memos;
import com.example.memoapp1.entity.Tags;
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
        model.addAttribute("memos", memoService.findAll());
        return "mypage";
    }  
    //新規メモ作成画面
    @GetMapping("/add")
    public String getCreatePage(Model model) {
    	model.addAttribute("memo" , new Memos());
    	model.addAttribute("tags" , tagService.getAllActiveTags());
    	return "memo-form";
    }
    
    //新規メモ作成処理
    @PostMapping("")
    public String postCreatePage(@ModelAttribute Memos memo,
                                 @RequestParam(value = "tagIds", required = false) List<Long> tagIds) {

        Set<Tags> selectedTags = new HashSet<>();
        if (tagIds != null && !tagIds.isEmpty()) {
            for (Long tagId : tagIds) {
                Tags tag = tagService.getTagById(tagId);
                if (tag != null) {
                    selectedTags.add(tag);
                }
            }
        }
        memo.setTags(selectedTags);
        memoService.save(memo);
        return "redirect:/memos";
    }
    
    //メモ詳細画面表示
    @GetMapping("/{id}")
    public String getMemoByld(@PathVariable Long id, Model model) {
    	Memos memo = memoService.findById(id);
    	model.addAttribute("memo", memo);
    	return "memo-detail";
    }
    
    //メモ編集画面表示
    @GetMapping("/{id}/edit")
    public String  getUpdatePage(@PathVariable Long id, Model model) {
    	Memos memo = memoService.findById(id);
    	model.addAttribute("memo", memo);
    	model.addAttribute("tags" , tagService.getAllActiveTags());
    	return "memo-form";
    }
    
    //メモ編集処理
    @PostMapping("/{id}/edit")
    public String postupdatePage(@PathVariable Long id,
                             @ModelAttribute Memos memo,
                             @RequestParam(value = "tagIds", required = false) List<Long> tagIds) {

        Memos existingMemo = memoService.findById(id);

        existingMemo.setTitle(memo.getTitle());
        existingMemo.setContent(memo.getContent());

        Set<Tags> selectedTags = new HashSet<>();
        if (tagIds != null && !tagIds.isEmpty()) {
            for (Long tagId : tagIds) {
                Tags tag = tagService.getTagById(tagId);
                if (tag != null) {
                    selectedTags.add(tag);
                }
            }
        }

        // タグの更新
        existingMemo.getTags().clear(); // 既存のタグをクリア
        existingMemo.setTags(selectedTags);

        memoService.update(existingMemo, selectedTags);

        return "redirect:/memos";
    }
    
    //メモ削除処理
    @PostMapping("/{id}/delete")
    public String deleteMemo(@PathVariable Long id) {
        memoService.delete(id);
        return "redirect:/memos";
    }
}