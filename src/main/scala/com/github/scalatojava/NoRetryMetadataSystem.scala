package com.github.scalatojava

import com.strobel.assembler.metadata.{ITypeLoader, MetadataSystem, TypeDefinition}

import scala.collection.mutable

class NoRetryMetadataSystem(typeLoader: ITypeLoader) extends MetadataSystem(typeLoader) {

	val failedTypes = mutable.Set[String]()

	override def resolveType(descriptor: String, mightBePrimitive: Boolean): TypeDefinition = {
		if (failedTypes.contains(descriptor)) {
			return null
		}
		val r = super.resolveType(descriptor, mightBePrimitive)
		if (r == null) {
			failedTypes.add(descriptor)
		}
		r
	}
}
