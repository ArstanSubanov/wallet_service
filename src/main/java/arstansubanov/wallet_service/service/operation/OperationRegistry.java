package arstansubanov.wallet_service.service.operation;

import arstansubanov.wallet_service.enums.OperationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OperationRegistry {

    private final Map<OperationType, Operation> operations = new HashMap<>();

    @Autowired
    public OperationRegistry(List<Operation> operations) {
        operations.forEach(operation -> this.operations.put(operation.getOperationType(), operation));
    }

    public Operation getOperation(OperationType operationType) {
        Operation operation = operations.get(operationType);
        if (operation == null) {
            throw new IllegalArgumentException("Operation type " + operationType + " not supported");
        }
        return operation;
    }
}
