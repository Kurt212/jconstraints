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
import gov.nasa.jpf.constraints.api.ExpressionVisitor;
import gov.nasa.jpf.constraints.api.Valuation;
import gov.nasa.jpf.constraints.api.Variable;
import gov.nasa.jpf.constraints.types.Type;

import java.io.IOException;
import java.util.Collection;

/**
* Array theory select expression
*/
public class SelectExpression<E> extends AbstractExpression<E> {
    public ArrayExpression<E> arrayExpression;
    public Expression<Integer> indexExpression;

    public SelectExpression(ArrayExpression<E> ae, Expression<Integer> ie) {
        this.arrayExpression = ae;
        this.indexExpression = ie;
    }

    public void print(Appendable a, int flags) throws IOException {
        arrayExpression.print(a, flags);
        a.append('[');
        indexExpression.print(a, flags);
        a.append(']');
    }

    public void collectFreeVariables(Collection<? super Variable<?>> variables) {
        indexExpression.collectFreeVariables(variables);
    }

    public E evaluate(Valuation values) {
        E ae = arrayExpression.evaluate(values);
        int ie = indexExpression.evaluate(values);
// TODO correct here
        return ae;
    }


    public Expression<?> duplicate(Expression<?>[] newChildren) {
        assert newChildren.length == 2;
        
        if (identical(newChildren, arrayExpression, indexExpression))
            return this;
        
        return new SelectExpression((ArrayExpression)newChildren[0], newChildren[1]);
    }

    public Expression<E>[] getChildren() {
        return new Expression[]{arrayExpression, indexExpression};
    }

    public Type<E> getType() {
        return arrayExpression.getType();
    }

    public <R, D> R accept(ExpressionVisitor<R, D> visitor, D data) {
        return visitor.visit(this, data);
    }
}


