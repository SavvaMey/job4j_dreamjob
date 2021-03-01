package servlet;

import junit.framework.TestCase;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import store.MemStore;
import store.PsqlStore;
import store.Store;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PsqlStore.class)
public class PostServletTest extends TestCase {

    @Test
    public void whenSavePostThenFindSuc() throws ServletException, IOException {
    HttpServletRequest req = mock(HttpServletRequest.class);
    HttpServletResponse resp = mock(HttpServletResponse.class);
    Store store = MemStore.instOf();
    PowerMockito.mockStatic(PsqlStore.class);
    when(PsqlStore.instOf()).thenReturn(store);
    when(req.getParameter("id")).thenReturn("0");
    when(req.getParameter("name")).thenReturn("java");
        new PostServlet().doPost(req, resp);
    assertThat(store.findByIdPost(1).getName(), is("java"));
    }
}