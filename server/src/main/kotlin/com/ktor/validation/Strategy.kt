//package com.ktor.validation
//
//import arrow.core.*
//import arrow.core.computations.either
//import arrow.typeclasses.Semigroup
//import io.ktor.utils.io.*
//
//sealed class Strategy {
//    object FailFast : Strategy()
//    object ErrorAccumulation : Strategy()
//}
//
//object Rules {
//    private fun RequestParam.required(): ValidatedNel<ValidationError, RequestParam> =
//        if (value?.isNotEmpty() == true) validNel()
//        else ValidationError.Empty(fieldName).invalidNel()
//
//    private fun RequestParam.minLength(size: Int) =
//        if (value?.length!! < size) ValidationError.MinLength(fieldName, size).invalidNel()
//        else validNel()
//
//    private fun RequestParam.validateErrorAccumulate(): ValidatedNel<ValidationError, RequestParam> {
//        return required()
//            .zip(minLength(3)) { _, _ -> this }
//            .zip(minLength(5)) { _, _ -> this }
//            .handleErrorWith {
//                ValidationError.InvalidRequest(it).invalidNel()
//            }
//    }
//
//    private fun RequestParam.validateFailFast(): Either<Nel<ValidationError>, RequestParam> {
//        val request = this
//
//        return either.eager {
//            required().bind()
//            minLength(5).bind()
//            request
//        }
//    }
//
//    operator fun invoke(
//        strategy: Strategy,
//        fields: List<RequestParam>
//    ): Either<Nel<ValidationError>, List<RequestParam>> =
//        when (strategy) {
//            Strategy.FailFast -> fields.traverseEither { it.validateFailFast() }
//            Strategy.ErrorAccumulation -> fields.traverseValidated(Semigroup.nonEmptyList()) {
//                it.validateErrorAccumulate()
//            }.toEither()
//        }
//}