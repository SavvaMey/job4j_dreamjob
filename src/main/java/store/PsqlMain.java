package store;

import model.Post;

public class PsqlMain {
    public static void main(String[] args) {
        Store store = PsqlStore.instOf();
        store.savePost(new Post(0, "Java Job"));
        for (Post post : store.findAllPosts()) {
            System.out.println(post.getId() + " " + post.getName());
        }
        System.out.println(store.findByIdPost(1).getName());
        store.savePost(new Post(1, "Java JobNew"));
        store.findAllPosts().stream().map(x -> x.getName() + x.getId())
                .forEach(System.out::println);
        System.out.println(store.findByIdPost(2).getName());
    }
}
