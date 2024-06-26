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
package com.facebook.presto.hive;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

/**
 * Identifies a piece of a file at a given path starting at a given position and continuing for length bytes.
 * This class does not check whether the file exists, or, if it exists, that it is long enough to
 * contain all the indicated bytes.
 */
public class HiveFileSplit
{
    private final String path;
    private final long start;
    private final long length;
    private final long fileSize;
    private final long fileModifiedTime;
    private final Optional<byte[]> extraFileInfo;
    private final Map<String, String> customSplitInfo;

    /**
     * @param path the absolute path to the file that contains the split
     * @param start the position in the containing file of the first byte of the split
     * @param length the number of bytes in the split
     * @param fileSize the total length of the file that contains the split
     * @param fileModifiedTime the most recent time when the file containing the split was modified
     * @throws NullPointerException if any object parameter is null
     * @throws IllegalArgumentException if any numeric parameter is less than zero
     */
    @JsonCreator
    public HiveFileSplit(
            @JsonProperty("path") String path,
            @JsonProperty("start") long start,
            @JsonProperty("length") long length,
            @JsonProperty("fileSize") long fileSize,
            @JsonProperty("fileModifiedTime") long fileModifiedTime,
            @JsonProperty("extraFileInfo") Optional<byte[]> extraFileInfo,
            @JsonProperty("customSplitInfo") Map<String, String> customSplitInfo)
    {
        checkArgument(start >= 0, "start must be non-negative");
        checkArgument(length >= 0, "length must be non-negative");
        checkArgument(fileSize >= 0, "fileSize must be non-negative");
        checkArgument(fileModifiedTime >= 0, "modificationTime must be non-negative");
        requireNonNull(path, "path is null");
        requireNonNull(extraFileInfo, "extraFileInfo is null");
        requireNonNull(customSplitInfo, "customSplitInfo is null");

        this.path = path;
        this.start = start;
        this.length = length;
        this.fileSize = fileSize;
        this.fileModifiedTime = fileModifiedTime;
        this.extraFileInfo = extraFileInfo;
        this.customSplitInfo = ImmutableMap.copyOf(customSplitInfo);
    }

    @JsonProperty
    public String getPath()
    {
        return path;
    }

    @JsonProperty
    public long getStart()
    {
        return start;
    }

    @JsonProperty
    public long getLength()
    {
        return length;
    }

    @JsonProperty
    public long getFileSize()
    {
        return fileSize;
    }

    @JsonProperty
    public long getFileModifiedTime()
    {
        return fileModifiedTime;
    }

    @JsonProperty
    public Optional<byte[]> getExtraFileInfo()
    {
        return extraFileInfo;
    }

    @JsonProperty
    public Map<String, String> getCustomSplitInfo()
    {
        return customSplitInfo;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        HiveFileSplit fileSplit = (HiveFileSplit) o;
        return start == fileSplit.start
                && length == fileSplit.length
                && fileSize == fileSplit.fileSize
                && fileModifiedTime == fileSplit.fileModifiedTime
                && Objects.equals(path, fileSplit.path)
                && Objects.equals(extraFileInfo, fileSplit.extraFileInfo)
                && Objects.equals(customSplitInfo, fileSplit.customSplitInfo);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(path, start, length, fileSize, fileModifiedTime, extraFileInfo, customSplitInfo);
    }

    @Override
    public String toString()
    {
        return "HiveFileSplit{" +
                "path='" + path + '\'' +
                ", start=" + start +
                ", length=" + length +
                ", fileSize=" + fileSize +
                ", fileModifiedTime=" + fileModifiedTime +
                ", extraFileInfo=" + extraFileInfo +
                ", customSplitInfo=" + customSplitInfo +
                '}';
    }
}
