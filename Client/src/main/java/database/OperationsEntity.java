package main.java.database;

import javax.persistence.*;
import java.util.Objects;

/**
 * Class represents entity from operations table
 * @author Michal Szeler
 * @version 6.0
 */
@Entity
@Table(name = "operations", schema = "operation_history", catalog = "")
public class OperationsEntity {
    /**
     * Field represent operation_ID column
     */
    private int operationId;
    /**
     * Field represent operation_type column
     */
    private String operationType;
    /**
     * Field represent input_text column
     */
    private String inputText;
    /**
     * Field represent operation_key column
     */
    private String operationKey;
    /**
     * Field represent output_text column
     */
    private String outputText;

    /**
     * Getter method for operation_id column
     * @return
     */
    @Id
    @Column(name = "operation_ID")
    public int getOperationId() {
        return operationId;
    }

    /**
     * Setter method for operation_id column
     * @return
     */
    public void setOperationId(int operationId) {
        this.operationId = operationId;
    }

    /**
     * Getter method for operation_type column
     * @return
     */
    @Basic
    @Column(name = "operation_type")
    public String getOperationType() {
        return operationType;
    }

    /**
     * Setter method for operation_type column
     * @return
     */
    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    /**
     * Getter method for input_text column
     * @return
     */
    @Basic
    @Column(name = "input_text")
    public String getInputText() {
        return inputText;
    }

    /**
     * Setter method for input_text column
     * @return
     */
    public void setInputText(String inputText) {
        this.inputText = inputText;
    }

    /**
     * Getter method for operation_key column
     * @return
     */
    @Basic
    @Column(name = "operation_key")
    public String getOperationKey() {
        return operationKey;
    }

    /**
     * Setter method for operation_key column
     * @return
     */
    public void setOperationKey(String operationKey) {
        this.operationKey = operationKey;
    }

    /**
     * Getter method for output_text column
     * @return
     */
    @Basic
    @Column(name = "output_text")
    public String getOutputText() {
        return outputText;
    }

    /**
     * Setter method for output_text column
     * @return
     */
    public void setOutputText(String outputText) {
        this.outputText = outputText;
    }

    /**
     * Method for comparing object with instance of class
     * @param o Object to be compared with instance
     * @return Value 0 if object contains different values/ is of different class
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OperationsEntity that = (OperationsEntity) o;
        return operationId == that.operationId &&
                Objects.equals(operationType, that.operationType) &&
                Objects.equals(inputText, that.inputText) &&
                Objects.equals(operationKey, that.operationKey) &&
                Objects.equals(outputText, that.outputText);
    }

    /**
     * Method that allows to get class instance hashcode
     * @return Hashcode of class instance
     */
    @Override
    public int hashCode() {
        return Objects.hash(operationId, operationType, inputText, operationKey, outputText);
    }
}
