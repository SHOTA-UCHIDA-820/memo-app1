package com.example.memoapp1.service;

import com.example.memoapp1.entity.Memos;
import com.example.memoapp1.entity.Tags;
import com.example.memoapp1.repository.MemoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MemoServiceの単体テスト")
class MemoServiceTest {

    @Mock
    private MemoRepository memoRepository;

    @InjectMocks
    private MemoService memoService;

    // =========================
    // 正常系
    // =========================

    @Test
    @DisplayName("IDを指定してメモを取得できる")
    void findById_正常系() {
        Memos memo = new Memos();
        memo.setTitle("テストタイトル");
        memo.setContent("テスト内容");
        when(memoRepository.findByIdWithTags(1L)).thenReturn(memo);

        Memos result = memoService.findById(1L);

        assertEquals(memo, result);
        assertEquals("テストタイトル", result.getTitle());
        verify(memoRepository).findByIdWithTags(1L);
    }

    @Test
    @DisplayName("全件取得できる")
    void findAll_正常系() {
        List<Memos> memos = List.of(new Memos(), new Memos());
        when(memoRepository.findAllWithTags()).thenReturn(memos);

        List<Memos> result = memoService.findAll();

        assertEquals(2, result.size());
        verify(memoRepository).findAllWithTags();
    }

    @Test
    @DisplayName("sortOrderがdescの場合、createdAtの降順になる")
    void searchMemos_descの場合_降順になる() {
        when(memoRepository.findAll(
                ArgumentMatchers.<Specification<Memos>>any(),
                any(Sort.class)
        )).thenReturn(Collections.emptyList());

        memoService.searchMemos("test", LocalDate.now(), LocalDate.now(), Set.of("tag1"), "desc");

        ArgumentCaptor<Sort> sortCaptor = ArgumentCaptor.forClass(Sort.class);
        verify(memoRepository).findAll(
                ArgumentMatchers.<Specification<Memos>>any(),
                sortCaptor.capture()
        );

        Sort sort = sortCaptor.getValue();
        assertTrue(sort.getOrderFor("createdAt").isDescending());
    }

    @Test
    @DisplayName("sortOrderがascの場合、createdAtの昇順になる")
    void searchMemos_ascの場合_昇順になる() {
        when(memoRepository.findAll(
                ArgumentMatchers.<Specification<Memos>>any(),
                any(Sort.class)
        )).thenReturn(Collections.emptyList());

        memoService.searchMemos("test", null, null, null, "asc");

        ArgumentCaptor<Sort> sortCaptor = ArgumentCaptor.forClass(Sort.class);
        verify(memoRepository).findAll(
                ArgumentMatchers.<Specification<Memos>>any(),
                sortCaptor.capture()
        );

        Sort sort = sortCaptor.getValue();
        assertTrue(sort.getOrderFor("createdAt").isAscending());
    }

    @Test
    @DisplayName("メモを保存できる")
    void save_正常系() {
        Memos memo = new Memos();
        when(memoRepository.save(memo)).thenReturn(memo);

        Memos result = memoService.save(memo);

        assertEquals(memo, result);
        verify(memoRepository).save(memo);
    }

    @Test
    @DisplayName("タグを更新できる")
    void update_正常系() {
        Memos memo = new Memos();
        Set<Tags> tags = Set.of(new Tags(), new Tags());

        when(memoRepository.save(memo)).thenReturn(memo);

        Memos result = memoService.update(memo, tags);

        assertEquals(tags, memo.getTags());
        assertEquals(memo, result);
        verify(memoRepository).save(memo);
    }

    @Test
    @DisplayName("IDを指定して削除できる")
    void delete_正常系() {
        memoService.delete(1L);
        verify(memoRepository).deleteById(1L);
    }

    // =========================
    // 異常系
    // =========================

    @Test
    @DisplayName("存在しないIDを指定した場合、nullが返る")
    void findById_存在しないIDの場合_nullが返る() {
        when(memoRepository.findByIdWithTags(999L)).thenReturn(null);

        Memos result = memoService.findById(999L);

        assertNull(result);
    }

    @Test
    @DisplayName("内容がnullでも保存できる")
    void save_内容がnullでも保存できる() {
        Memos memo = new Memos();
        memo.setTitle("タイトル");
        memo.setContent(null);

        when(memoRepository.save(memo)).thenReturn(memo);

        Memos result = memoService.save(memo);

        assertEquals(memo, result);
    }

    @Test
    @DisplayName("タグがnullでも更新できる")
    void update_tagsがnullでもOK() {
        Memos memo = new Memos();
        memo.setTitle("タイトル");

        when(memoRepository.save(memo)).thenReturn(memo);

        Memos result = memoService.update(memo, null);

        assertNull(memo.getTags());
        assertEquals(memo, result);
    }

    @Test
    @DisplayName("Repositoryで例外が発生した場合、そのまま伝播する")
    void delete_Repositoryで例外が起きたらそのまま投げられる() {
        doThrow(new RuntimeException("削除失敗"))
                .when(memoRepository).deleteById(1L);

        assertThrows(RuntimeException.class, () -> {
            memoService.delete(1L);
        });
    }

    @Test
    @DisplayName("検索結果が空の場合、空のリストが返る")
    void searchMemos_Repositoryが空リストを返した場合() {
        when(memoRepository.findAll(
                ArgumentMatchers.<Specification<Memos>>any(),
                any(Sort.class)
        )).thenReturn(Collections.emptyList());

        List<Memos> result = memoService.searchMemos(null, null, null, null, null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}


