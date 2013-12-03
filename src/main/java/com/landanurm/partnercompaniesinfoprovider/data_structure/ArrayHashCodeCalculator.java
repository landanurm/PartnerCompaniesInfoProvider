package com.landanurm.partnercompaniesinfoprovider.data_structure;

/**
 * Created by Leonid on 02.12.13.
 */
class ArrayHashCodeCalculator {
    static <T> int hashCodeOf(T[] array) {
        int totalHashCode = 0;
        for (Object each : array) {
            totalHashCode += each.hashCode();
        }
        return totalHashCode;
    }
}
