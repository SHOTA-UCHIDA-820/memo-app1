package com.example.memoapp1.controller;

import com.example.memoapp1.entity.Memos;
import com.example.memoapp1.entity.Tags;
import com.example.memoapp1.service.MemoService;
import com.example.memoapp1.service.TagService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/memo")
public class MemoController {

    private final MemoService memoService;
    private final TagService tagService;

    public MemoController(MemoService memoService, TagService tagService) {
        this.memoService = memoService;
        this.tagService = tagService;
    }

    // メモ一覧表示
    @GetMapping("/list")
    public String listMemos(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "startDate", required = false) String startDateStr,
            @RequestParam(value = "endDate", required = false) String endDateStr,
            @RequestParam(value = "tags", required = false) Set<String> selectedTags,
            @RequestParam(value = "sort", required = false) String sortOrder,
            Model model) {

        LocalDate startDate = (startDateStr == null || startDateStr.isBlank()) ? null : LocalDate.parse(startDateStr);
        LocalDate endDate = (endDateStr == null || endDateStr.isBlank()) ? null : LocalDate.parse(endDateStr);

        List<Memos> memos = memoService.searchMemos(title, startDate, endDate, selectedTags, sortOrder);
        model.addAttribute("memos", memos);
        model.addAttribute("tags", tagService.getAllTags());

        model.addAttribute("currentTitle", title != null ? title : "");
        model.addAttribute("currentStartDate", startDateStr != null ? startDateStr : "");
        model.addAttribute("currentEndDate", endDateStr != null ? endDateStr : "");
        model.addAttribute("currentSelectedTags", selectedTags != null ? selectedTags : new HashSet<>());
        model.addAttribute("currentSort", sortOrder != null ? sortOrder : "");

        return "memo/list";
    }

    // メモ登録画面
    @GetMapping("/add")
    public String showAddPage(Model model) {
    	if (!model.containsAttribute("memo")) {
            model.addAttribute("memo", new Memos());
        }
        model.addAttribute("tags", tagService.getAllTags());
        return "memo/edit";
    }

    // メモ登録処理
    @PostMapping("/add")
    public String addMemo(@Valid @ModelAttribute("memo") Memos memo, BindingResult result,
                          @RequestParam(value = "tagIds", required = false) List<Long> tagIds, Model model) {
    	
    	 if (result.hasErrors()) {
    	        // エラーがあれば保存せず画面に戻す
    	        model.addAttribute("tags", tagService.getAllTags());
    	        model.addAttribute("memo", memo);
    	        return "memo/edit";
    	    }
    	 

        Set<Tags> selectedTags = new HashSet<>();
        if (tagIds != null) {
            for (Long id : tagIds) {
                Tags tag = tagService.getTagById(id);
                if (tag != null) selectedTags.add(tag);
            }
        }
        memo.setTags(selectedTags);
        memoService.save(memo);
        return "redirect:/memo/list";
    }

    // メモ編集画面
    @GetMapping("/edit")
    public String showEditPage(@RequestParam("id") Long id, Model model) {
        Memos memo = memoService.findById(id);
        if (memo == null) {
            // 存在しないIDなら新規に作る or エラー画面へ
            memo = new Memos();
        }
        model.addAttribute("memo", memo);
        model.addAttribute("tags", tagService.getAllTags());
        return "memo/edit";
    }

    // メモ編集処理
    @PostMapping("/edit")
    public String editMemo(@RequestParam("id") Long id,
    		               @Valid @ModelAttribute("memo") Memos memo, BindingResult result,
                           @RequestParam(value = "tagIds", required = false) List<Long> tagIds, Model model) {
    	
    	if (result.hasErrors()) {
            model.addAttribute("tags", tagService.getAllTags());
            model.addAttribute("memo", memo);
            return "memo/edit";
        }

        Memos existingMemo = memoService.findById(id);
        existingMemo.setTitle(memo.getTitle());
        existingMemo.setContent(memo.getContent());

        Set<Tags> selectedTags = new HashSet<>();
        if (tagIds != null) {
            for (Long tid : tagIds) {
                Tags tag = tagService.getTagById(tid);
                if (tag != null) selectedTags.add(tag);
            }
        }
        existingMemo.setTags(selectedTags);
        memoService.update(existingMemo, selectedTags);

        return "redirect:/memo/list";
    }

    // メモ詳細表示
    @GetMapping("/view")
    public String viewMemo(@RequestParam("id") Long id, Model model) {
        Memos memo = memoService.findById(id);
        model.addAttribute("memo", memo);
        return "memo/view";
    }

    // メモ削除
    @PostMapping("/delete")
    public String deleteMemo(@RequestParam("id") Long id) {
        memoService.delete(id);
        return "redirect:/memo/list";
    }
}


