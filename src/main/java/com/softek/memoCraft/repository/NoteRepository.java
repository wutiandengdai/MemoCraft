package com.softek.memoCraft.repository;

import com.softek.memoCraft.model.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface NoteRepository extends PagingAndSortingRepository<Note, Long>,
        JpaRepository<Note, Long> {

    @Query("SELECT n FROM Note n JOIN n.tags t WHERE t IN :tags GROUP BY n.id HAVING COUNT(t) = :tagCount ORDER BY n.createdDate DESC")
    Page<Note> findByLabels(@Param("tags") Set<String> labels, @Param("tagCount") Long tagCount, Pageable pageable);
}