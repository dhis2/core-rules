package org.hisp.dhis.rules.models

data class RuleActionWarningOnCompletion(override var content: String?, override var data: String?, override var field: String?) : RuleActionMessage() {
    companion object {

        fun create(content: String?,
                   data: String?, field: String?): RuleActionWarningOnCompletion {
            if (content == null && data == null && field == null) {
                throw Exception("Content, data and field" + " must not be null at the same time")
            }

            return RuleActionWarningOnCompletion(content ?: "",
                    data ?: "", field ?: "")
        }
    }
}