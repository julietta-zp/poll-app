package xyz.hedo.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import xyz.hedo.entity.Poll;
import xyz.hedo.repository.PollRepository;
import xyz.hedo.util.Ajax;
import xyz.hedo.util.ExceptionHandlerController;
import xyz.hedo.util.RestException;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * @author panic
 */
@RestController
public class PollController extends ExceptionHandlerController {

    @Autowired
    @Qualifier("pollRepository")
    private PollRepository pollRepository;

    /**
     * Get all created polls
     * @return Map json response data
     * @throws RestException
     */
    @RequestMapping(value = "/polls", method = RequestMethod.GET)
    public Map<String, Object> getAllPolls() throws RestException{
        try{
            Iterable<Poll> allPolls = pollRepository.findAll();
            for(Poll poll : allPolls){
                updatePollResourceWithLinks(poll);
            }
            return Ajax.successResponse(allPolls);
        }catch (Exception e){
            throw new RestException(e);
        }
    }

    /**
     * Create new poll
     * @param poll valid poll object
     * @return Map json response data
     * @throws RestException
     */
    @RequestMapping(value="/polls",	method=RequestMethod.POST)
    public Map<String, Object> createPoll(@Valid @RequestBody Poll poll) throws RestException{
        try {
            poll = pollRepository.save(poll);
            // generate hash for particular poll (id+email+timestamp)
            String hash = hashValues(poll.getPollId(), poll.getEmail(), poll.getCreatedAt().toString());
            poll.setHash(hash);
            pollRepository.save(poll);
            updatePollResourceWithLinks(poll);
            return Ajax.successResponse(poll);
        } catch (Exception e){
            throw new RestException(e);
        }
    }

    /**
     * Get poll with particular hash
     * @param pollHash hash generated for particular poll
     * @return Map json response data
     * @throws RestException
     */
    @RequestMapping(value="/polls/{pollHash}", method=RequestMethod.GET)
    public Map<String, Object> getPoll(@PathVariable String pollHash) throws RestException{
        try {
            if (pollHash == null){
                return Ajax.errorResponse("Incorrect data");
            }
            Poll poll = pollRepository.findByHash(pollHash);
            // if no poll with such hash was found
            if (poll == null){
                return Ajax.errorResponse("No such poll found");
            }
            updatePollResourceWithLinks(poll);
            return Ajax.successResponse(poll);
        } catch (Exception e){
            throw new RestException(e);
        }
    }

    /**
     * Update poll
     * @param poll valid poll object
     * @param pollHash hash generated for particular poll
     * @return Map json response data
     * @throws RestException
     */
    @RequestMapping(value = "/polls/{pollHash}", method = RequestMethod.PUT)
    public Map<String, Object> updatePoll(@Valid @RequestBody Poll poll, @PathVariable String pollHash) throws RestException{
        try {
            if (pollHash == null){
                return Ajax.errorResponse("Incorrect data");
            }
            Poll p = pollRepository.findByHash(pollHash);
            // if no poll with such hash was found
            if (p == null){
                return Ajax.errorResponse("No such poll found");
            }
            updatePollResourceWithLinks(poll);
            pollRepository.save(poll);
            return Ajax.emptyResponse();
        } catch (Exception e){
            throw new RestException(e);
        }
    }

    /**
     * Close poll (set {@link Poll#closed} to true)
     * @param pollHash hash generated for particular poll
     * @return Map json response data
     * @throws RestException
     */
    @RequestMapping(value = "/close", method = RequestMethod.PUT)
    public Map<String, Object> closePoll(@RequestParam String pollHash) throws RestException{
        try {
            if (pollHash == null){
                return Ajax.errorResponse("Incorrect data");
            }
            Poll poll = pollRepository.findByHash(pollHash);
            // if no poll with such hash was found
            if (poll == null){
                return Ajax.errorResponse("No such poll found");
            }
            poll.setClosed(true);
            pollRepository.save(poll);
            return Ajax.emptyResponse();
        } catch (Exception e){
            throw new RestException(e);
        }
    }

    /*@RequestMapping(value = "/polls/{pollId}", method = RequestMethod.DELETE)
    public Map<String, Object> deletePoll(@PathVariable Integer pollId) throws RestException{
        try {
            if (pollId <= 0){
                return Ajax.errorResponse("Value of id cannot be less than or equal 0");
            }
            pollRepository.delete(pollId);
            return Ajax.emptyResponse();
        } catch (Exception e){
            throw new RestException(e);
        }
    }*/

    /**
     * Set embedded link to poll object
     * @param poll valid poll object
     * @throws RestException
     */
    private void updatePollResourceWithLinks(Poll poll) throws RestException{
        poll.add(linkTo(methodOn(PollController.class).getAllPolls()).slash(poll.getHash()).withSelfRel());
        poll.add(linkTo(methodOn(VoteController.class).getAllVotes(poll.getHash())).withRel("vote"));
        poll.add(linkTo(methodOn(ResultsController.class).getResults(poll.getHash())).withRel("results"));
        poll.add(linkTo(methodOn(PollController.class).closePoll(poll.getHash())).withRel("close-poll"));
    }

    /**
     * Generate hash for each poll
     * @param pollId id
     * @param email author_email
     * @param timestamp created_at
     * @return hash
     * @throws RestException
     */
    private String hashValues(Integer pollId, String email, String timestamp) throws RestException{
        StringBuilder hashedValue = null;
        String allValues = pollId + email + timestamp;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(allValues.getBytes("UTF-8"));
            hashedValue = new StringBuilder();
            for (int i: hash) {
                hashedValue.append(Integer.toHexString(0XFF & i));
            }
        }catch (NoSuchAlgorithmException | UnsupportedEncodingException e){
            throw new RestException(e);
        }
        return hashedValue.toString();
    }

}
