package com.kozich.voting.controller.http;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.kozich.voting.controller.factory.ControllerFactory;
import com.kozich.voting.dao.api.dto.StatDTO;
import com.kozich.voting.service.api.StatService;
import com.kozich.voting.service.api.VoteService;
import com.kozich.voting.service.api.dto.VoteDTO;
import com.kozich.voting.service.factory.ServiceFactorySingleton;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class VotingServlet extends HttpServlet {

    private final VoteService voteService = ServiceFactorySingleton.getVoteService();
    private final StatService statService = ServiceFactorySingleton.getStatService();
    private final ObjectMapper mapper = ControllerFactory.getMapper();

    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp)
            throws ServletException, IOException {

        VoteDTO vote = mapper.readValue(req.getInputStream(), VoteDTO.class);

        this.voteService.save(vote);

        StatDTO stat = this.statService.get();

        resp.getWriter().write(mapper.writeValueAsString(stat));
        resp.setStatus(HttpServletResponse.SC_CREATED);
    }
}
