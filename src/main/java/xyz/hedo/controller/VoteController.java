package xyz.hedo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import xyz.hedo.entity.Poll;
import xyz.hedo.entity.Vote;
import xyz.hedo.repository.PollRepository;
import xyz.hedo.repository.VoteRepository;
import xyz.hedo.util.Ajax;
import xyz.hedo.util.ExceptionHandlerController;
import xyz.hedo.util.RestException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

/**
 * @author panic
 */
@RestController
public class VoteController extends ExceptionHandlerController {

    @Autowired
    @Qualifier("voteRepository")
    private VoteRepository voteRepository;
    @Autowired
    @Qualifier("pollRepository")
    private PollRepository pollRepository;

    /**
     * Vote for particular option in the poll
     * @param pollHash hash of the poll
     * @param vote valid vote object
     * @param request HttpServletRequest
     * @return Map json response data
     * @throws RestException
     */
    @RequestMapping(value="/polls/{pollHash}/votes", method = RequestMethod.POST)
    public Map<String, Object> createVote(@PathVariable String pollHash, @Valid @RequestBody Vote vote,
                                          HttpServletRequest request) throws RestException{
       try {
           Poll poll = pollRepository.findByHash(pollHash);
           // if poll is closed
           if (poll.isClosed()){
               return Ajax.errorResponse("You can't vote. The poll was closed.");
           }
           String ip = request.getRemoteAddr();
           // if this ip has already been used for voting
           if (voteRepository.findByIpAndPoll(ip, pollHash).iterator().hasNext()){
               return Ajax.errorResponse("Thank you. Your vote has been counted. You can't vote twice.");
           }
           vote.setIp(ip);
           vote = voteRepository.save(vote);
           return Ajax.successResponse(vote);
       }catch (Exception e){
           throw new RestException(e);
       }
    }

    /**
     * Get all votes of the poll
     * @param pollHash hash of the poll
     * @return Map json response object
     */
    @RequestMapping(value="/polls/{pollHash}/votes", method=RequestMethod.GET)
    public	Map<String, Object>	getAllVotes(@PathVariable String pollHash){
        Iterable<Vote> votes = voteRepository.findByPoll(pollHash);
        return Ajax.successResponse(votes);
    }
}
