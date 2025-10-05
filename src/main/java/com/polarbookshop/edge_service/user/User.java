package com.polarbookshop.edge_service.user;

import java.util.List;

public record User(
        String username,
        String fullName,
        String lastName,
        List<String> roles
) {
    
}
