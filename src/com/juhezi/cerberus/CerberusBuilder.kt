package com.juhezi.cerberus

/**
 * Created by qiao1 on 2017/2/24.
 */
class CerberusBuilder {
    private val TAG = "CerberusBuilder"

    fun build(): Cerberus {
        return Cerberus(this)
    }

}