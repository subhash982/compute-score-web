/**
 * 
 */
package io.coding.excercise.score.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.coding.excercise.score.exception.ComputeScoreException;
import io.coding.excercise.score.helper.FileProcessingHelper;
import io.coding.excercise.score.modal.ComputeScoreResponse;
import io.coding.excercise.score.service.ComputeScoreService;

/**
 * @author subhash
 *
 */
@RestController
@RequestMapping("/api")
public class ComputeScoreController {

	@Autowired
	private ComputeScoreService computeScoreService;
	
	@Autowired
	private FileProcessingHelper	 fileProcessingHelper;
	
	/**
	 * Rest resource to compute the score.
	 * 
	 * @param file
	 * @param algorithm
	 * @return
	 */
	@RequestMapping(method = { RequestMethod.POST }, path = { "/score" })
	public ComputeScoreResponse computeScore(@RequestParam("file") MultipartFile file,@RequestParam("algorithm") String algorithm) {
		if(fileProcessingHelper.isValidFile(file.getOriginalFilename())) {
			String targetFileLocation = fileProcessingHelper.storeFile(file);
			long computedScore = computeScoreService.compute(targetFileLocation, algorithm);
			
			return new ComputeScoreResponse(file.getOriginalFilename(), computedScore, file.getSize());
		}else {
			throw new ComputeScoreException("Invalide File, Please select valid txt / csv file.");
		}

	}

}
