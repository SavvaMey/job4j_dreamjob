package store;

import model.Candidate;
import model.Post;
import model.User;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MemStore implements Store {
    private static final Store INST = new MemStore();

    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();
    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();
    private static AtomicInteger postId = new AtomicInteger(0);
    private static AtomicInteger candidateId = new AtomicInteger(0);
    private static AtomicInteger cityId = new AtomicInteger(0);

    private MemStore() {
//        posts.put(1, new Post(1, "Junior Java Job"));
//        posts.put(2, new Post(2, "Middle Java Job"));
//        posts.put(3, new Post(3, "Senior Java Job"));
//        candidates.put(1, new Candidate(1, "Junior Java", 0));
//        candidates.put(2, new Candidate(2, "Middle Java", 0));
//        candidates.put(3, new Candidate(3, "Senior Java", 0));
    }

    public static Store instOf() {
        return INST;
    }

    public Collection<Post> findAllPosts() {
        return posts.values();
    }

    public Collection<Candidate> findAllCandidates() {
        return candidates.values();
    }

    @Override
    public void savePost(Post post) {
        if (post.getId() == 0) {
            post.setId(postId.incrementAndGet());
        }
        posts.put(post.getId(), post);
    }

    @Override
    public void saveCandidate(Candidate candidate) {

    }

    @Override
    public Post findByIdPost(int id) {
       return posts.get(id);
    }

    public void save(Post post) {
        if (post.getId() == 0) {
            post.setId(postId.incrementAndGet());
        }
        posts.put(post.getId(), post);
    }

//    public Post findById(int id) {
//        return posts.get(id);
//    }


    public void save(Candidate candidate) {
        if (candidate.getId() == 0) {
            candidate.setId(candidateId.incrementAndGet());
        }
        candidates.put(candidate.getId(), candidate);
    }

    public Candidate findByIdCandidate(int id) {
        return candidates.get(id);
    }

    @Override
    public String getImage(int id) {
        return null;
    }

    @Override
    public int saveImage(String name) {
        return 0;
    }

    @Override
    public void updateCandidatePhoto(int idCandidate, int idPhoto) {

    }

    @Override
    public void deletePhoto(int idPhoto) {

    }

    @Override
    public void deleteCan(int idCandidate) {

    }

    @Override
    public User createUser(User user) {
        return null;
    }

    @Override
    public void updateUser(User user) {

    }

    @Override
    public User findByIdUser(int id) {
        return null;
    }

    @Override
    public Collection<User> findAllUsers() {
        return null;
    }

    @Override
    public User findByEmailUser(String email) {
        return null;
    }

    @Override
    public int saveCity(String name) {
        return 0;
    }

    @Override
    public void updateCandidateCity(int idCandidate, int idCity) {

    }

    @Override
    public void deleteCity(int idCity) {

    }

    @Override
    public Collection<String> findAllCities() {
        return null;
    }

    @Override
    public String findByIdCity(int id) {
        return null;
    }
}
