package com.lsgllc.norm.kernel.graph.model.instance.types;

import com.lsgllc.norm.kernel.graph.typing.INormType;
import org.objectweb.asm.Opcodes;

/**
 * Created By: sameloyiv
 * Date: 2/3/13
 * Time: 2:45 PM
 * <p/>
 * <p/>
 * (c) Texas Department of Motor Vehicles  2013
 * ---------------------------------------------------------------------
 * Change History:
 * Name		    Date		Description
 * ------------	-----------	--------------------------------------------
 *
 * @author
 * @description
 * @date
 */
public interface INormInstanceType<T>{
    String getClassName();
    Opcodes getASMOpCode();
    Class<T> getClazz();
}
