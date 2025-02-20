// Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
// or more contributor license agreements. Licensed under the Elastic License
// 2.0; you may not use this file except in compliance with the Elastic License
// 2.0.
package org.elasticsearch.xpack.esql.expression.function.scalar.math;

import java.lang.Override;
import java.lang.String;
import org.elasticsearch.compute.data.Block;
import org.elasticsearch.compute.data.DoubleBlock;
import org.elasticsearch.compute.data.DoubleVector;
import org.elasticsearch.compute.data.Page;
import org.elasticsearch.compute.operator.DriverContext;
import org.elasticsearch.compute.operator.EvalOperator;
import org.elasticsearch.core.Releasables;

/**
 * {@link EvalOperator.ExpressionEvaluator} implementation for {@link Round}.
 * This class is generated. Do not edit it.
 */
public final class RoundDoubleNoDecimalsEvaluator implements EvalOperator.ExpressionEvaluator {
  private final EvalOperator.ExpressionEvaluator val;

  private final DriverContext driverContext;

  public RoundDoubleNoDecimalsEvaluator(EvalOperator.ExpressionEvaluator val,
      DriverContext driverContext) {
    this.val = val;
    this.driverContext = driverContext;
  }

  @Override
  public Block.Ref eval(Page page) {
    try (Block.Ref valRef = val.eval(page)) {
      DoubleBlock valBlock = (DoubleBlock) valRef.block();
      DoubleVector valVector = valBlock.asVector();
      if (valVector == null) {
        return Block.Ref.floating(eval(page.getPositionCount(), valBlock));
      }
      return Block.Ref.floating(eval(page.getPositionCount(), valVector).asBlock());
    }
  }

  public DoubleBlock eval(int positionCount, DoubleBlock valBlock) {
    try(DoubleBlock.Builder result = DoubleBlock.newBlockBuilder(positionCount, driverContext.blockFactory())) {
      position: for (int p = 0; p < positionCount; p++) {
        if (valBlock.isNull(p) || valBlock.getValueCount(p) != 1) {
          result.appendNull();
          continue position;
        }
        result.appendDouble(Round.process(valBlock.getDouble(valBlock.getFirstValueIndex(p))));
      }
      return result.build();
    }
  }

  public DoubleVector eval(int positionCount, DoubleVector valVector) {
    try(DoubleVector.Builder result = DoubleVector.newVectorBuilder(positionCount, driverContext.blockFactory())) {
      position: for (int p = 0; p < positionCount; p++) {
        result.appendDouble(Round.process(valVector.getDouble(p)));
      }
      return result.build();
    }
  }

  @Override
  public String toString() {
    return "RoundDoubleNoDecimalsEvaluator[" + "val=" + val + "]";
  }

  @Override
  public void close() {
    Releasables.closeExpectNoException(val);
  }

  static class Factory implements EvalOperator.ExpressionEvaluator.Factory {
    private final EvalOperator.ExpressionEvaluator.Factory val;

    public Factory(EvalOperator.ExpressionEvaluator.Factory val) {
      this.val = val;
    }

    @Override
    public RoundDoubleNoDecimalsEvaluator get(DriverContext context) {
      return new RoundDoubleNoDecimalsEvaluator(val.get(context), context);
    }

    @Override
    public String toString() {
      return "RoundDoubleNoDecimalsEvaluator[" + "val=" + val + "]";
    }
  }
}
