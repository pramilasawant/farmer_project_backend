package com.shadervertex.farmerproduct.services.admin.faq;

import com.shadervertex.farmerproduct.dto.FAQDto;

public interface FAQService {
	FAQDto postFAQ(Long productId, FAQDto faqDto);
}
