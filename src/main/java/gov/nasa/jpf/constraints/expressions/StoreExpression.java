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
* Array theory store expression
*/
public class StoreExpression<E> extends AbstractBoolExpression {
    public ArrayExpression<E[]> arrayExpression;
    public Expression<Integer> indexExpression;
    public Expression<E> value;
    public ArrayExpression<E[]> newArrayExpression;

    public StoreExpression(ArrayExpression<E[]> ae, Expression<Integer> ie, Expression<E> v, ArrayExpression<E[]> nae) {
        this.arrayExpression = ae;
        this.indexExpression = ie;
        this.value = v;
        this.newArrayExpression = nae;
    }

    public void print(Appendable a, int flags) throws IOException {
        newArrayExpression.print(a, flags);
        a.append('[');
        indexExpression.print(a, flags);
        a.append("] = ");
        value.print(a, flags);
    }

    public void collectFreeVariables(Collection<? super Variable<?>> variables) {
        arrayExpression.collectFreeVariables(variables);
        indexExpression.collectFreeVariables(variables);
        value.collectFreeVariables(variables);
        newArrayExpression.collectFreeVariables(variables);
    }

    public Boolean evaluate(Valuation values) {
        E[] ae = arrayExpression.evaluate(values);
        int ie = indexExpression.evaluate(values);
        E val = value.evaluate(values);
        ae[ie] = val;
        E[] nae = newArrayExpression.evaluate(values);
// TODO correct here
        return (nae == ae);
    }


    public Expression<?> duplicate(Expression<?>[] newChildren) {
        assert newChildren.length == 4;
        
        if (identical(newChildren, arrayExpression, indexExpression, value, newArrayExpression))
            return this;
        
        return new StoreExpression((ArrayExpression)newChildren[0], newChildren[1], newChildren[2], (ArrayExpression)newChildren[3]);
    }

    public Expression<E>[] getChildren() {
        return new Expression[]{arrayExpression, indexExpression, value, newArrayExpression};
    }


    public <R, D> R accept(ExpressionVisitor<R, D> visitor, D data) {
        return visitor.visit(this, data);
    }
}


