package store;

import model.Candidate;
import model.Post;
import model.User;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store {
    private static final Logger LOG = LoggerFactory.getLogger(PsqlStore.class.getName());
    private final BasicDataSource pool = new BasicDataSource();

    private PsqlStore() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(
                new FileReader("db.properties")
        )) {
            cfg.load(io);
        } catch (Exception e) {
            LOG.error("properties ex", e);
        }
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            LOG.error("driver ex", e);
        }
        pool.setDriverClassName(cfg.getProperty("jdbc.driver"));
        pool.setUrl(cfg.getProperty("jdbc.url"));
        pool.setUsername(cfg.getProperty("jdbc.username"));
        pool.setPassword(cfg.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
    }

    private static final class Lazy {
        private static final Store INST = new PsqlStore();
    }

    public static Store instOf() {
        return Lazy.INST;
    }

    @Override
    public Collection<Post> findAllPosts() {
        List<Post> posts = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "SELECT * FROM post")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    posts.add(new Post(it.getInt("id"),
                            it.getString("name")));
                }
            }
        } catch (Exception e) {
            LOG.error("db ex", e);
        }
        return posts;
    }

    @Override
    public Collection<Candidate> findAllCandidates() {
        List<Candidate> candidates = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "SELECT * FROM candidate")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    candidates.add(
                            new Candidate(it.getInt("id"),
                                    it.getString("name"),
                                    it.getInt(3),
                                    it.getInt(4))
                    );
                }
            }
        } catch (Exception e) {
            LOG.error("db ex", e);
        }
        return candidates;
    }

    @Override
    public void savePost(Post post) {
        if (post.getId() == 0) {
            create(post);
        } else {
            update(post);
        }
    }

    private Post create(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "INSERT INTO post(name) VALUES (?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, post.getName());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    post.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error("db ex", e);
        }
        return post;
    }

    private void update(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement statement = cn.prepareStatement(
                     "UPDATE post set name=? where id=? ")) {
            statement.setString(1, post.getName());
            statement.setInt(2, post.getId());
            int row = statement.executeUpdate();
        } catch (SQLException throwable) {
            LOG.error("db ex", throwable);
        }
    }

    @Override
    public Post findByIdPost(int id) {
        Post post = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement statement = cn.prepareStatement(
                     "SELECT * FROM post WHERE id=?")) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    post = new Post(resultSet.getInt(1),
                            resultSet.getString(2));
                }
            }
        } catch (SQLException throwable) {
            LOG.error("db ex", throwable);
        }
        return post;
    }

    @Override
    public void saveCandidate(Candidate candidate) {
        if (candidate.getId() == 0) {
            create(candidate);
        } else {
            update(candidate);
        }
    }

    private Candidate create(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "INSERT INTO candidate(name, cityId) VALUES (?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, candidate.getName());
            ps.setInt(2, candidate.getCityId());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    candidate.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error("db ex", e);
        }
        return candidate;
    }

    private void update(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement statement = cn.prepareStatement(
                     "UPDATE candidate set name = (?), cityId = (?) where id= (?) ")) {
            statement.setString(1, candidate.getName());
            statement.setInt(2, candidate.getCityId());
            statement.setInt(3, candidate.getId());
            statement.executeUpdate();
        } catch (SQLException throwable) {
            LOG.error("db ex", throwable);
        }
    }

    @Override
    public Candidate findByIdCandidate(int id) {
        Candidate candidate = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement statement = cn.prepareStatement(
                     "SELECT * FROM candidate WHERE id= (?)")) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    candidate = new Candidate(resultSet.getInt(1),
                            resultSet.getString(2),
                            resultSet.getInt(3),
                            resultSet.getInt(4));
                }
            }
        } catch (SQLException throwable) {
            LOG.error("db ex", throwable);
        }
        return candidate;
    }

    @Override
    public String getImage(int id) {
        String result = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement statement = cn.prepareStatement(
                     "SELECT * FROM photos WHERE id = (?)")
        ) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    result = resultSet.getString(2);
                }
            }
        } catch (Exception e) {
            LOG.error("db ex", e);
        }
        return result;
    }

    @Override
    public int saveImage(String name) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "INSERT INTO photos (name) VALUES (?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    return id.getInt(1);
                }
            }
        } catch (SQLException throwables) {
            LOG.error("db ex", throwables);
        }
        return -1;
    }

    @Override
    public void updateCandidatePhoto(int idCandidate, int idPhoto) {
        try (Connection cn = pool.getConnection();
             PreparedStatement statement = cn.prepareStatement(
                     "UPDATE candidate set photoId= (?) where id= (?)")) {
            statement.setInt(1, idPhoto);
            statement.setInt(2, idCandidate);
            statement.executeUpdate();
        } catch (SQLException throwable) {
            LOG.error("db ex", throwable);
        }
    }

    @Override
    public void deletePhoto(int idPhoto) {
        try (Connection cn = pool.getConnection();
             PreparedStatement statement = cn.prepareStatement(
                     "DELETE FROM photos where id= (?)")) {
            statement.setInt(1, idPhoto);
            statement.executeUpdate();
        } catch (SQLException throwable) {
            LOG.error("db ex", throwable);
        }
    }

    @Override
    public void deleteCan(int idCandidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement statement = cn.prepareStatement(
                     "DELETE FROM candidate where id= (?)")) {
            statement.setInt(1, idCandidate);
            statement.executeUpdate();
        } catch (SQLException throwable) {
            LOG.error("db ex", throwable);
        }
    }

    @Override
    public User createUser(User user) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("INSERT INTO users"
                             + "(name, email, password) VALUES (?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    user.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error("db ex", e);
        }
        return user;
    }

    @Override
    public void updateUser(User user) {
        try (Connection cn = pool.getConnection();
             PreparedStatement statement = cn.prepareStatement(
                     "UPDATE users set name = (?), email = (?), password = (?)"
                             + " where id= (?) ")) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setInt(4, user.getId());
            statement.executeUpdate();
        } catch (SQLException throwable) {
            LOG.error("db ex", throwable);
        }
    }

    @Override
    public User findByIdUser(int id) {
        User user = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement statement = cn.prepareStatement(
                     "SELECT * FROM users WHERE id=?")) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    user = new User(resultSet.getInt(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getString(4));
                }
            }
        } catch (SQLException throwable) {
            LOG.error("db ex", throwable);
        }
        return user;
    }

    @Override
    public Collection<User> findAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "SELECT * FROM users")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    users.add(
                            new User(it.getInt(1),
                                    it.getString(2),
                                    it.getString(3),
                                    it.getString(4)));
                }
            }
        } catch (Exception e) {
            LOG.error("db ex", e);
        }
        return users;
    }

    @Override
    public User findByEmailUser(String email) {
        User user = null;
        try (Connection connection = pool.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "select * from users where email = (?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = new User(
                            Integer.parseInt(rs.getString("id")),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("password"));
                }
            }
        } catch (Exception e) {
            LOG.error("db ex", e);
        }
        return user;
    }

    @Override
    public int saveCity(String nameCity) {
        int id = -1;
        try (Connection connection = pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "insert into city (name) values (?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, nameCity);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                id = resultSet.getInt("city_id");
            }
        } catch (SQLException e) {
            LOG.error("db ex", e);
        }
        return id;
    }

    @Override
    public void updateCandidateCity(int idCandidate, int idCity) {
        try (Connection cn = pool.getConnection();
             PreparedStatement statement = cn.prepareStatement(
                     "UPDATE candidate set city_id = (?) where id= (?)")) {
            statement.setInt(1, idCity);
            statement.setInt(2, idCandidate);
            statement.executeUpdate();
        } catch (SQLException throwable) {
            LOG.error("db ex", throwable);
        }
    }

    @Override
    public void deleteCity(int idCity) {
        try (Connection cn = pool.getConnection();
             PreparedStatement statement = cn.prepareStatement(
                     "DELETE FROM city where id= (?)")) {
            statement.setInt(1, idCity);
            statement.executeUpdate();
        } catch (SQLException throwable) {
            LOG.error("db ex", throwable);
        }
    }

    @Override
    public Collection<String> findAllCities() {
        List<String> cities = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "SELECT * FROM city")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    cities.add(it.getString("name"));
                }
            }
        } catch (Exception e) {
            LOG.error("db ex", e);
        }
        return cities;
    }

    @Override
    public String findByIdCity(int id) {
        String city = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement statement = cn.prepareStatement(
                     "SELECT * FROM city WHERE id = (?)")) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    city = resultSet.getString(2);
                }
            }
        } catch (SQLException throwable) {
            LOG.error("db ex", throwable);
        }
        return city;
    }
}