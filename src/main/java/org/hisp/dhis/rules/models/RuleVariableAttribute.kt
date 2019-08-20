package org.hisp.dhis.rules.models

data class RuleVariableAttribute(
        var rName: String?,
        val trackedEntityAttribute: String?,
        val trackedEntityAttributeType: RuleValueType?
) : RuleVariable(rName) {

    companion object {

        fun create(name: String,
                   attribute: String, attributeType: RuleValueType): RuleVariableAttribute {
            return RuleVariableAttribute(name, attribute, attributeType)
        }
    }
}