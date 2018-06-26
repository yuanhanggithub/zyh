package com.mirror.util.clear;

public class SdcardEntity
{
    double blockCount;
    double blockSize;
    double currentCount;
    double currentSize;

    public SdcardEntity(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
    {
        this.blockCount = paramDouble1;
        this.blockSize = paramDouble2;
        this.currentCount = paramDouble3;
        this.currentSize = paramDouble4;
    }

    public double getBlockCount()
    {
        return this.blockCount;
    }

    public double getBlockSize()
    {
        return this.blockSize;
    }

    public double getCurrentCount()
    {
        return this.currentCount;
    }

    public double getCurrentSize()
    {
        return this.currentSize;
    }

    public void setBlockCount(double paramDouble)
    {
        this.blockCount = paramDouble;
    }

    public void setBlockSize(double paramDouble)
    {
        this.blockSize = paramDouble;
    }

    public void setCurrentCount(double paramDouble)
    {
        this.currentCount = paramDouble;
    }

    public void setCurrentSize(double paramDouble)
    {
        this.currentSize = paramDouble;
    }
}
