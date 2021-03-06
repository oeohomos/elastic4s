package com.sksamuel.elastic4s.http.search.queries

import com.sksamuel.elastic4s.http.EnumConversions
import com.sksamuel.elastic4s.json.{XContentBuilder, XContentFactory}
import com.sksamuel.elastic4s.searches.sort.{FieldSortDefinition, SortDefinition}

object SortContentBuilder {
  def apply(sort: SortDefinition): XContentBuilder = sort match {
    case fs: FieldSortDefinition => FieldSortContentBuilder(fs)
  }
}

object FieldSortContentBuilder {
  def apply(fs: FieldSortDefinition): XContentBuilder = {

    val builder = XContentFactory.jsonBuilder().startObject(fs.field)

    fs.unmappedType.foreach(builder.field("unmapped_type", _))
    fs.missing.foreach(builder.autofield("missing", _))
    fs.sortMode.map(EnumConversions.sortMode).foreach(builder.field("mode", _))
    builder.field("order", EnumConversions.order(fs.order))
    fs.nestedPath.foreach(builder.field("nested_path", _))
    fs.nestedFilter.map(QueryBuilderFn.apply).map(_.string).foreach(builder.rawField("nested_filter", _))

    builder.endObject().endObject()
  }
}
