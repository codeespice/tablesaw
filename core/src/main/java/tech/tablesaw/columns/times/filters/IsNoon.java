/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tech.tablesaw.columns.times.filters;

import tech.tablesaw.api.ColumnType;
import tech.tablesaw.api.DateTimeColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.api.TimeColumn;
import tech.tablesaw.columns.Column;
import tech.tablesaw.columns.ColumnReference;
import tech.tablesaw.columns.datetimes.PackedLocalDateTime;
import tech.tablesaw.columns.times.PackedLocalTime;
import tech.tablesaw.filtering.ColumnFilter;
import tech.tablesaw.selection.Selection;

public class IsNoon extends ColumnFilter {

    public IsNoon(ColumnReference reference) {
        super(reference);
    }

    @Override
    public Selection apply(Table relation) {
        String name = columnReference().getColumnName();
        Column column = relation.column(name);
        return apply(column);
    }

    @Override
    public Selection apply(Column column) {
        ColumnType type = column.type();
        switch (type) {
            case LOCAL_TIME:
                TimeColumn timeColumn = (TimeColumn) column;
                return timeColumn.eval(PackedLocalTime::isNoon);
            case LOCAL_DATE_TIME:
                DateTimeColumn dateTimeColumn = (DateTimeColumn) column;
                return dateTimeColumn.eval(PackedLocalDateTime::isNoon);
            default:
                throw new UnsupportedOperationException("Columns of type " + type.name() + " do not support the operation "
                        + "isNoon() ");
        }
    }
}
