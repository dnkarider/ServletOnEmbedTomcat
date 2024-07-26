package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class PostRepositoryStubImpl implements PostRepository {
    //    private final Set<Post> posts = Collections.synchronizedSet(new HashSet());
    private final ConcurrentHashMap<Long, Post> posts = new ConcurrentHashMap<>();
    private List<Long> emptyIds = new ArrayList<>();
    private long idTracker;

    public ConcurrentHashMap<Long, Post> all() {
        return posts;
    }

    public Optional<Post> getById(long id) {
        if (posts.containsKey(id)) {
            return Optional.of(posts.get(id));
        }
        return Optional.empty();
    }


    public Post save(Post post) {
        if (post.getId() == 0) {
            if (!emptyIds.isEmpty()) {
                long id = emptyIds.remove(0);
                posts.put(id, post);
                post.setId(id);
                return post;
            }
            posts.put((idTracker + 1), post);
            post.setId(idTracker + 1);
            idTracker++;
        } else if (post.getId() <= idTracker && post.getId() != 0) {
            if (emptyIds.contains(post.getId())) {
                emptyIds.remove(post.getId());
                posts.put(post.getId(), post);
                return post;//проблема может быть в порядке листа, когда нужно будет достать элемент по индексу
            }
            if (posts.containsKey(post.getId())) {
                posts.put(post.getId(), post);
            }
        } else if (post.getId() > idTracker && post.getId() != 0) {
            if (!emptyIds.isEmpty()) {
                posts.put(emptyIds.get(0), post);
                post.setId(emptyIds.remove(0));
                return post;
            }
            posts.put(idTracker + 1, post);
            post.setId(idTracker + 1);
            idTracker++;
        }
        return post;
    }

    //
    public void removeById(long id) {
        if (id < idTracker + 1 && id != 0) {
            if (posts.containsKey(id)) {
                posts.remove(id);
                emptyIds.add(id);
            }
        }
    }
}
