package servlet;

import model.Candidate;
import store.MemStore;
import store.PsqlStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class CandidateServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Collection<Candidate> candidates = PsqlStore.instOf().findAllCandidates();
        req.setAttribute("candidates", candidates);
        Map<Integer, String> map = new HashMap<>();
        candidates.stream().map(Candidate::getCityId).
                forEach(id -> map.put(id, PsqlStore.instOf().findByIdCity(id)));
        req.setAttribute("user", req.getSession().getAttribute("user"));
        req.setAttribute("cities", map);
        req.getRequestDispatcher("candidates.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        Candidate candidate = new Candidate(Integer.parseInt(req.getParameter("id")),
                req.getParameter("name"),
                0, Integer.parseInt(req.getParameter("cityValue")));
        PsqlStore.instOf().saveCandidate(candidate);
        resp.sendRedirect(req.getContextPath() + "/upload" + "?candidateId=" + candidate.getId());
    }
}
