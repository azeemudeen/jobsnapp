package com.jobsnapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jobsnapp.model.Picture;
import com.jobsnapp.model.Post;

public interface PictureRepository extends JpaRepository<Picture, Long> {
}
