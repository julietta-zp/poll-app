package xyz.hedo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.hedo.entity.Vote;
import xyz.hedo.entity.dto.OptionCount;
import xyz.hedo.entity.dto.VoteResult;
import xyz.hedo.repository.VoteRepository;
import xyz.hedo.util.Ajax;
import xyz.hedo.util.ExceptionHandlerController;
import xyz.hedo.util.RestException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author panic
 */
@RestController
public class ResultsController extends ExceptionHandlerController{

    @Autowired
    @Qualifier("voteRepository")
    private VoteRepository voteRepository;

    /**
     * Get results of the poll
     * @param pollHash hash of the poll
     * @return Map json response data
     * @throws RestException
     */
    @RequestMapping(value="/results", method= RequestMethod.GET)
    public Map<String, Object> getResults(@RequestParam String pollHash) throws RestException{
        try{
            VoteResult voteResult = new VoteResult();
            Iterable<Vote> allVotes = voteRepository.findByPoll(pollHash);
            int	totalVotes = 0;
            Map<Integer, OptionCount> tempMap = new HashMap<>();
            for(Vote v : allVotes) {
                totalVotes++;
                OptionCount	optionCount = tempMap.get(v.getOption().getId());
                if(optionCount	==	null) {
                    optionCount	= new OptionCount();
                    optionCount.setOptionId(v.getOption().getId());
                    tempMap.put(v.getOption().getId(), optionCount);
                }
                optionCount.setCounter(optionCount.getCounter()+1);
            }
            voteResult.setTotalVotes(totalVotes);
            voteResult.setResults(tempMap.values());
            return Ajax.successResponse(voteResult);
        }catch (Exception e){
            throw new RestException(e);
        }
    }
}
