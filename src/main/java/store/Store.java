package store;

import model.Candidate;
import model.Post;
import model.User;

import java.util.Collection;

public interface Store {
    Collection<Post> findAllPosts();

    Collection<Candidate> findAllCandidates();

    void savePost(Post post);

    void saveCandidate(Candidate candidate);

    Post findByIdPost(int id);

    Candidate findByIdCandidate(int id);

    String getImage(int id);

    int saveImage(String name);

    void updateCandidatePhoto(int idCandidate, int idPhoto);

    void deletePhoto(int idPhoto);

    void deleteCan(int idCandidate);

    User createUser(User user);

    void updateUser(User user);

    User findByIdUser(int id);

    Collection<User> findAllUsers();

//    void deleteUser(int idUser);
}
