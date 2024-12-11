package com.shadervertex.farmerproduct.controller.admin;

import com.shadervertex.farmerproduct.dto.FarmerProductRequestDto;
import com.shadervertex.farmerproduct.services.admin.farmer.FarmerProductRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/farmer/requests")
public class FarmerProductRequestController {

    private final FarmerProductRequestService requestService;

    public FarmerProductRequestController(FarmerProductRequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping("/pending")
    public ResponseEntity<List<FarmerProductRequestDto>> getPendingRequests() {
        List<FarmerProductRequestDto> requests = requestService.getPendingRequests();
        return ResponseEntity.ok(requests);
    }

    @PostMapping("/approve/{requestId}")
    public ResponseEntity<Void> approveRequest(@PathVariable Long requestId) {
        try {
            requestService.approveRequest(requestId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/reject/{requestId}")
    public ResponseEntity<Void> rejectRequest(@PathVariable Long requestId) {
        try {
            requestService.rejectRequest(requestId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}

