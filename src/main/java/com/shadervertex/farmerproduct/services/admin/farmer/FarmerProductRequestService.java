package com.shadervertex.farmerproduct.services.admin.farmer;

import com.shadervertex.farmerproduct.dto.FarmerProductRequestDto;
import com.shadervertex.farmerproduct.model.FarmerProductRequest;
import com.shadervertex.farmerproduct.model.Product;
import com.shadervertex.farmerproduct.model.User;
import com.shadervertex.farmerproduct.repository.FarmerProductRequestRepository;
import com.shadervertex.farmerproduct.repository.ProductRepository;
import com.shadervertex.farmerproduct.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FarmerProductRequestService {

    @Autowired
    private FarmerProductRequestRepository requestRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    public void createProductRequest(FarmerProductRequest request) {
        requestRepository.save(request);
    }

    public List<FarmerProductRequestDto> getPendingRequests() {
        List<FarmerProductRequest> requests = requestRepository.findAllByIsApproved(false);
        return requests.stream()
                       .map(this::convertToDto)
                       .collect(Collectors.toList());
    }

    private FarmerProductRequestDto convertToDto(FarmerProductRequest request) {
        return FarmerProductRequestDto.builder()
                                     .id(request.getId())
                                     .name(request.getName())
                                     .price(request.getPrice())
                                     .description(request.getDescription())
                                     .img(request.getImg())
                                     .categoryId(request.getCategory().getId())
                                     .categoryName(request.getCategory().getName())
                                     .isApproved(request.getIsApproved())
                                     .submittedBy(request.getSubmittedBy() != null ? request.getSubmittedBy().getId() : null)
                                     .build();
    }

   public void approveRequest(Long requestId) {
    Optional<FarmerProductRequest> requestOpt = requestRepository.findById(requestId);
    if (requestOpt.isPresent()) {
        FarmerProductRequest request = requestOpt.get();
        request.setIsApproved(true);
        requestRepository.save(request);

        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());
        product.setImg(request.getImg());
        product.setCategory(request.getCategory());

        // Set the submittedBy field with the user (farmer) who submitted the request
        if (request.getSubmittedBy() != null) {
            Optional<User> farmerOpt = userRepository.findById(request.getSubmittedBy().getId());
            farmerOpt.ifPresent(product::setSubmittedBy);  // Corrected method reference
        }

        productRepository.save(product);
    } else {
        throw new RuntimeException("Product request not found with ID: " + requestId);
    }
}


    public void rejectRequest(Long requestId) {
        if (requestRepository.existsById(requestId)) {
            requestRepository.deleteById(requestId);
        } else {
            throw new RuntimeException("Product request not found with ID: " + requestId);
        }
    }
}

