/*
 * Copyright (C) 2015, United States Government, as represented by the 
 * Administrator of the National Aeronautics and Space Administration.
 * All rights reserved.
 *
 * The PSYCO: A Predicate-based Symbolic Compositional Reasoning environment 
 * platform is licensed under the Apache License, Version 2.0 (the "License"); you 
 * may not use this file except in compliance with the License. You may obtain a 
 * copy of the License at http://www.apache.org/licenses/LICENSE-2.0. 
 *
 * Unless required by applicable law or agreed to in writing, software distributed 
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR 
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the 
 * specific language governing permissions and limitations under the License.
 */

package gov.nasa.jpf.constraints.expressions;

import gov.nasa.jpf.constraints.api.Expression;
import gov.nasa.jpf.constraints.api.Variable;
import gov.nasa.jpf.constraints.types.BuiltinTypes;
import gov.nasa.jpf.constraints.types.Type;

import java.io.IOException;

public class ArrayExpression<E> extends Variable<E> {
    public Expression<Integer> length;
    private String elemType = "?";

    public ArrayExpression(Type<E> type, String name) {
        super(type, name);
        this.length = Variable.create(BuiltinTypes.SINT32, name+"_length");
    }

    public ArrayExpression(Type<E> type, String name, int l) {
        super(type, name);
        this.length = Constant.create(BuiltinTypes.SINT32, l);
    }

    public ArrayExpression(Type<E> type, String name, String arrayType) {
        super(type, name);
        this.length = Variable.create(BuiltinTypes.SINT32, name+"_length");
        this.elemType = arrayType;
    }

    public static <E> String getNewName(ArrayExpression<E> prev) {
        String newName = prev.getName();
        if (newName.indexOf("!") == -1) {
            newName = newName + "!1";
        } else {
            int aux = Integer.parseInt(newName.substring(newName.indexOf("!") + 1));
            newName = newName.substring(0, newName.indexOf("!") + 1) + (aux + 1);
        }
        return newName;
    }

    public ArrayExpression(ArrayExpression<E> prev) {
        super(prev.getType(), getNewName(prev));
        this.length = prev.length;
        this.elemType = prev.getElemType();
    }
        
    public String getElemType() {
        return elemType;
    }

    public static <E> ArrayExpression<E> create(Type<E> type, String name) {
        return new ArrayExpression<E>(type, name);
    }

    public static <E> ArrayExpression<E> create(Type<E> type, String name, String arrayType) {
        return new ArrayExpression<E>(type, name, arrayType);
    }

    public static <E> ArrayExpression<E> create(Type<E> type, String name, int l) {
       return new ArrayExpression<E>(type, name, l);
    }

    public void print(Appendable a, int flags) throws IOException {
        a.append(this.getName());
    }

}
