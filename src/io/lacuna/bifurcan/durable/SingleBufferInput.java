package io.lacuna.bifurcan.durable;

import io.lacuna.bifurcan.DurableInput;
import io.lacuna.bifurcan.durable.allocator.SlabAllocator;

import java.nio.ByteBuffer;

public class SingleBufferInput implements DurableInput {

  private final ByteBuffer buf;
  private final Slice bounds;

  public SingleBufferInput(ByteBuffer buf, Slice bounds) {
    this.buf = buf;
    this.bounds = bounds;
  }

  @Override
  public Slice bounds() {
    return bounds;
  }

  @Override
  public DurableInput slice(long start, long end) {
    if (start == 0) {
      new Throwable().printStackTrace();
    }
    return new SingleBufferInput(
        ((ByteBuffer) buf.duplicate()
            .clear()
            .position((int) start)
            .limit((int) end))
            .slice(),
        new Slice(bounds, start, end));
  }

  @Override
  public DurableInput duplicate() {
    return new SingleBufferInput(buf.duplicate(), bounds);
  }

  @Override
  public DurableInput seek(long position) {
    buf.position((int) position);
    return this;
  }

  @Override
  public long remaining() {
    return buf.remaining();
  }

  @Override
  public long position() {
    return buf.position();
  }

  @Override
  public int read(ByteBuffer dst) {
    return Util.transfer(buf, dst);
  }

  @Override
  public void close() {
    SlabAllocator.tryFree(buf);
  }

  @Override
  public byte readByte() {
    return buf.get();
  }

  @Override
  public short readShort() {
    return buf.getShort();
  }

  @Override
  public char readChar() {
    return buf.getChar();
  }

  @Override
  public int readInt() {
    return buf.getInt();
  }

  @Override
  public long readLong() {
    return buf.getLong();
  }

  @Override
  public float readFloat() {
    return buf.getFloat();
  }

  @Override
  public double readDouble() {
    return buf.getDouble();
  }
}