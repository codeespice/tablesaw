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

package tech.tablesaw.columns;

import it.unimi.dsi.fastutil.ints.IntComparator;
import tech.tablesaw.api.ColumnType;
import tech.tablesaw.api.Table;
import tech.tablesaw.selection.Selection;
import tech.tablesaw.table.RollingColumn;

/**
 * The general interface for columns.
 * <p>
 * Columns can either exist on their own or be a part of a table. All the data in a single column is of a particular
 * type.
 */
public interface Column {

    int size();

    Table summary();

    default Column subset(Selection rows) {
        Column c = this.emptyCopy();
        for (int row : rows) {
            c.appendCell(getString(row));
        }
        return c;
    }

    /**
     * Returns the count of missing values in this column.
     *
     * @return missing values as int
     */
    int countMissing();

    /**
     * Returns the count of unique values in this column.
     *
     * @return unique values as int
     */
    int countUnique();

    /**
     * Returns a column of the same type as the receiver, containing only the unique values of the receiver.
     *
     * @return a {@link Column}
     */
    Column unique();

    /*
     */

    /**
     * Returns a column of the same type as the receiver, containing the receivers values offset -n
     * For example if you lead a column containing 2, 3, 4 by 1, you get a column containing 3, 4, NA.
     */

    default Column lead(int n) {
        return lag(-n);
    }

    /**
     * Returns a column of the same type and size as the receiver, containing the receivers values offset by n.
     * <p>
     * For example if you lag a column containing 2, 3, 4 by 1, you get a column containing NA, 2, 3
     */

    Column lag(int n);

    /**
     * Returns the column's name.
     *
     * @return name as String
     */
    String name();

    /**
     * Sets the columns name to the given string
     *
     * @param name The new name MUST be unique for any table containing this column
     * @return this Column to allow method chaining
     */
    Column setName(String name);

    /**
     * Returns this column's ColumnType
     *
     * @return {@link ColumnType}
     */
    ColumnType type();

    /**
     * Returns a string representation of the value at the given row.
     *
     * @param row The index of the row.
     * @return value as String
     */
    String getString(int row);

    /**
     * Returns a double representation of the value at the given row. The nature of the returned value is column-specific.
     * The double returned MAY be the actual value (for Number columns) but is more likely a number that maps to the column
     * value in some way. 
     *
     * @param row The index of the row.
     * @return value as String
     */
    double getDouble(int row);

    /**
     * Returns a copy of the receiver with no data. The column name and type are the same.
     *
     * @return a empty copy of {@link Column}
     */
    Column emptyCopy();

    /**
     * Returns a deep copy of the receiver
     *
     * @return a {@link Column}
     */
    Column copy();

    /**
     * Returns an empty copy of the receiver, with its internal storage initialized to the given row size.
     *
     * @param rowSize the initial row size
     * @return a {@link Column}
     */
    Column emptyCopy(int rowSize);

    void clear();

    void sortAscending();

    void sortDescending();

    /**
     * Returns true if the column has no data
     *
     * @return true if empty, false if not
     */
    boolean isEmpty();

    void appendCell(String stringValue);

    IntComparator rowComparator();

    void append(Column column);

    default Column first(int numRows) {
        Column col = emptyCopy();
        int rows = Math.min(numRows, size());
        for (int i = 0; i < rows; i++) {
            col.appendCell(getUnformattedString(i));
        }
        return col;
    }

    default Column last(int numRows) {
        Column col = emptyCopy();
        int rows = Math.min(numRows, size());
        for (int i = size() - rows; i < size(); i++) {
            col.appendCell(getUnformattedString(i));
        }
        return col;
    }

    /**
     * TODO(lwhite): Print n from the top and bottom, like a table;
     */
    String print();

    default String title() {
        return "Column: " + name() + '\n';
    }

    default double[] asDoubleArray() {
        throw new UnsupportedOperationException("Method asDoubleArray() is not supported on non-numeric columns");
    }

    int columnWidth();

    Selection isMissing();

    Selection isNotMissing();

    /**
     * Returns the width of a cell in this column, in bytes.
     *
     * @return width in bytes
     */
    int byteSize();

    /**
     * Returns the contents of the cell at rowNumber as a byte[].
     *
     * @param rowNumber index of the row
     * @return content as byte[]
     */
    byte[] asBytes(int rowNumber);

    default RollingColumn rolling(int windowSize) {
        return new RollingColumn(this, windowSize);
    }

    String getUnformattedString(int r);

    boolean isMissing(int rowNumber);

    /**
     * Appends a missing value appropriate to the column
     */
    void appendMissing();
}
