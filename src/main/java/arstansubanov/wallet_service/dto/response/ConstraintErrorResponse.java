package arstansubanov.wallet_service.dto.response;

import java.util.List;

public record ConstraintErrorResponse(
        List<Violation> violations
) {
}
