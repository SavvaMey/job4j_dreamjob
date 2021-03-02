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

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
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

    @Test
    public void whenDoGet() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        HttpSession httpSession = mock(HttpSession.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        Store store = MemStore.instOf();
        PowerMockito.mockStatic(PsqlStore.class);
        when(PsqlStore.instOf()).thenReturn(store);
        when(req.getSession()).thenReturn(httpSession);
        when(req.getRequestDispatcher("posts.jsp")).thenReturn(dispatcher);
        new PostServlet().doGet(req, resp);
        verify(req).getRequestDispatcher("posts.jsp");
        verify(req, times(1)).getSession();
        verify(dispatcher).forward(req, resp);
    }


}