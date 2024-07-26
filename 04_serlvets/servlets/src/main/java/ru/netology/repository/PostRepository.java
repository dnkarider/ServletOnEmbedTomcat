package ru.netology.repository;

import ru.netology.model.Post;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public interface PostRepository {
    ConcurrentHashMap<Long, Post> all();

    Optional<Post> getById(long id);

    Post save(Post post);

    void removeById(long id);
}
