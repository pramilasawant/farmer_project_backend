package com.shadervertex.farmerproduct.services.admin.faq;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.shadervertex.farmerproduct.dto.FAQDto;
import com.shadervertex.farmerproduct.model.FAQ;
import com.shadervertex.farmerproduct.repository.FAQRepository;
import com.shadervertex.farmerproduct.repository.ProductRepository;
import com.shadervertex.farmerproduct.model.*;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FAQServiceImpl implements FAQService{
	private final FAQRepository faqRepository;	
	private final ProductRepository productRepository;
	
	public FAQDto postFAQ(Long productId, FAQDto faqDto) {
		Optional<Product> optionalProduct = productRepository.findById(productId);
		if(optionalProduct.isPresent()) {
			FAQ faq = new FAQ();
			faq.setQuestion(faqDto.getQuestion());
			faq.setAnswer(faqDto.getAnswer());
			faq.setProduct(optionalProduct.get());
			
			return faqRepository.save(faq).getFAQDto();
		}
		return null;
	}
}